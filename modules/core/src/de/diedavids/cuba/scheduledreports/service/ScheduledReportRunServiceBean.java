package de.diedavids.cuba.scheduledreports.service;

import com.haulmont.cuba.core.app.ServerConfig;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.reports.ReportingApi;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.entity.ReportTemplate;
import de.diedavids.cuba.scheduledreports.ScheduledReportExtension;
import de.diedavids.cuba.scheduledreports.ScheduledReportExtensionFactory;
import de.diedavids.cuba.scheduledreports.core.DefaultScheduledReportParameterExtension;
import de.diedavids.cuba.scheduledreports.core.ScheduledReportEmailing;
import de.diedavids.cuba.scheduledreports.core.ScheduledReportRepository;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution;
import de.diedavids.cuba.scheduledreports.events.ScheduledReportRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service(ScheduledReportRunService.NAME)
public class ScheduledReportRunServiceBean implements ScheduledReportRunService {


    private static final Logger log = LoggerFactory.getLogger(ScheduledReportRunServiceBean.class);

    @Inject
    protected ScheduledReportRepository scheduledReportRepository;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected TimeSource timeSource;
    @Inject
    protected Events events;
    @Inject
    protected ScheduledReportExtensionFactory scheduledReportExtensionFactory;
    @Inject
    protected ReportingApi reportingApi;

    @Inject
    protected ScheduledReportEmailing scheduledReportEmailing;

    @Inject
    protected ServerConfig serverConfig;

    @Override
    public boolean isSchedulingSystemActive() {
        return serverConfig.getSchedulingActive();
    }

    @Override
    public void runScheduledReport(String scheduledReportId) {
        runReport(parseReportId(scheduledReportId));
    }

    private UUID parseReportId(String scheduledReportId) {
        try {
            return UUID.fromString(scheduledReportId);
        }
        catch (Exception e) {
            log.error("Scheduled Report ID: {} is not a valid UUID", scheduledReportId);
            throw e;
        }
    }

    private void runReport(UUID scheduledReportId) {
        Optional<ScheduledReport> scheduledReport = scheduledReportRepository.loadById(scheduledReportId, "scheduled-report-with-executions");

        if (scheduledReport.isPresent()) {
            runReport(scheduledReport.get());
        }
        else {
            log.error("Scheduled report with ID not found: {}. Scheduled Report cannot be executed", scheduledReportId);
        }
    }

    private void runReport(ScheduledReport scheduledReport) {

        ScheduledReportExecution scheduledReportExecution = createExecution(scheduledReport);

        ScheduledReportExtension extension = getExtension(scheduledReport);
        boolean shouldBeExecuted = extension
                .shouldBeExecuted(scheduledReport);

        if (shouldBeExecuted) {
            doRunReport(scheduledReport, scheduledReportExecution);
        }
        else {
            log.info("execution skipped for scheduled report: {}", scheduledReport);
        }
    }

    private void doRunReport(ScheduledReport scheduledReport, ScheduledReportExecution scheduledReportExecution) {
        try {
            Report report = scheduledReport.getReport();
            ReportTemplate reportTemplate = getReportTemplate(scheduledReport, report);
            scheduledReportExecution.setReportFile(generateReportFile(scheduledReport, report, reportTemplate));
            scheduledReportExecution.setSuccessful(true);
        } catch (Exception e) {
            log.error("error while creating report file for scheduled report: '" + scheduledReport.getName() + "'", e);
            scheduledReportExecution.setSuccessful(false);
        }
        ScheduledReportExecution persistedReportExecution = persistExecution(scheduledReportExecution);

        triggerEmailSendingIfNecessary(scheduledReport, persistedReportExecution);

        notifySystemAboutOutcome(scheduledReportExecution);

    }

    private ScheduledReportExecution persistExecution(ScheduledReportExecution scheduledReportExecution) {
        scheduledReportExecution.setExecutedAt(timeSource.currentTimestamp());
        return dataManager.commit(scheduledReportExecution);
    }

    private void triggerEmailSendingIfNecessary(ScheduledReport scheduledReport, ScheduledReportExecution scheduledReportExecution) {
        if (Boolean.TRUE.equals(scheduledReport.getSendEmail()) && scheduledReport.getEmailTemplate() != null) {
            scheduledReportEmailing.sendEmailForScheduledReport(
                    scheduledReport.getEmailTemplate(),
                    scheduledReportExecution
            );
        }
    }


    private void notifySystemAboutOutcome(ScheduledReportExecution scheduledReportExecution) {
        events.publish(new ScheduledReportRun(this, scheduledReportExecution.getReportFile(), scheduledReportExecution));
    }

    private FileDescriptor generateReportFile(ScheduledReport scheduledReport, Report report, ReportTemplate reportTemplate) {
        FileDescriptor savedReport;
        savedReport = reportingApi.createAndSaveReport(
                report,
                reportTemplate,
                getReportParameters(scheduledReport),
                getFilename(scheduledReport, reportTemplate)
        );
        return savedReport;
    }

    private ScheduledReportExecution createExecution(ScheduledReport scheduledReport) {
        ScheduledReportExecution scheduledReportExecution = dataManager.create(ScheduledReportExecution.class);
        scheduledReportExecution.setScheduledReport(scheduledReport);
        return scheduledReportExecution;
    }

    private ReportTemplate getReportTemplate(ScheduledReport scheduledReport, Report report) {

        Optional<ReportTemplate> reportTemplateFromExtension = getExtension(scheduledReport)
                .provideReportTemplate(scheduledReport, report);

        return reportTemplateFromExtension
                .orElseGet(() -> getDefaultReportTemplate(scheduledReport, report));
    }

    private ReportTemplate getDefaultReportTemplate(ScheduledReport scheduledReport, Report report) {
        Optional<ReportTemplate> reportTemplate = defaultExtension()
                .provideReportTemplate(scheduledReport, report);

        return reportTemplate.orElse(null);
    }

    private DefaultScheduledReportParameterExtension defaultExtension() {
        return new DefaultScheduledReportParameterExtension();
    }

    private String getFilename(ScheduledReport scheduledReport, ReportTemplate reportTemplate) {
        ScheduledReportExtension extension = getExtension(scheduledReport);
        Optional<String> optionalFilename = extension.provideFilename(scheduledReport, reportTemplate);


        return optionalFilename
                .orElseGet(() -> defaultExtension().provideFilename(scheduledReport, reportTemplate)
                .orElse(null));
    }

    private Map<String, Object> getReportParameters(ScheduledReport scheduledReport) {
        ScheduledReportExtension extension = getExtension(scheduledReport);

        Map<String, Object> possibleReportParameters = extension.provideParameters(scheduledReport);

        return Optional.ofNullable(possibleReportParameters)
                .orElseGet(() -> defaultExtension().provideParameters(scheduledReport));
    }

    private ScheduledReportExtension getExtension(ScheduledReport scheduledReport) {
        return scheduledReportExtensionFactory.create(scheduledReport);
    }
}