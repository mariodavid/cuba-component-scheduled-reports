package de.diedavids.cuba.scheduledreports.service;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.reports.ReportingApi;
import com.haulmont.reports.app.service.ReportService;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.entity.ReportTemplate;
import de.diedavids.cuba.scheduledreports.ScheduledReportExtension;
import de.diedavids.cuba.scheduledreports.ScheduledReportExtensionFactory;
import de.diedavids.cuba.scheduledreports.core.DefaultScheduledReportParameterExtension;
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


    @Override
    public void runScheduledReport(String code) {

        ScheduledReport scheduledReport = scheduledReportRepository.loadByCode(code, "scheduled-report-with-executions");

        runReport(scheduledReport);
    }

    private void runReport(ScheduledReport scheduledReport) {

        Report report = scheduledReport.getReport();

        ScheduledReportExecution scheduledReportExecution = dataManager.create(ScheduledReportExecution.class);
        scheduledReportExecution.setScheduledReport(scheduledReport);

        boolean shouldBeExecuted = getExtension(scheduledReport).shouldBeExecuted(scheduledReport);

        if (shouldBeExecuted) {

            FileDescriptor savedReport = null;
            try {
                ReportTemplate reportTemplate = getReportTemplate(scheduledReport, report);
                savedReport = reportingApi.createAndSaveReport(
                        report,
                        reportTemplate,
                        getParams(scheduledReport),
                        getFilename(scheduledReport, reportTemplate)
                );
                scheduledReportExecution.setReportFile(savedReport);
                scheduledReportExecution.setSuccessful(true);
            } catch (Exception e) {
                scheduledReportExecution.setSuccessful(false);
            }

            scheduledReportExecution.setExecutedAt(timeSource.currentTimestamp());

            dataManager.commit(scheduledReportExecution);

            events.publish(new ScheduledReportRun(this, savedReport, scheduledReportExecution));

        }
        else {
            log.info("execution skipped for scheduled report: {}", scheduledReport);
        }
    }

    private ReportTemplate getReportTemplate(ScheduledReport scheduledReport, Report report) {

        Optional<ReportTemplate> reportTemplateFromExtension = getExtension(scheduledReport).provideReportTemplate(scheduledReport, report);

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


        return optionalFilename.orElseGet(() -> defaultExtension().provideFilename(scheduledReport, reportTemplate).orElse(null));
    }

    private Map<String, Object> getParams(ScheduledReport scheduledReport) {
        ScheduledReportExtension extension = getExtension(scheduledReport);

        return extension.provideParameters(scheduledReport);
    }

    private ScheduledReportExtension getExtension(ScheduledReport scheduledReport) {
        return scheduledReportExtensionFactory.create(scheduledReport);
    }
}