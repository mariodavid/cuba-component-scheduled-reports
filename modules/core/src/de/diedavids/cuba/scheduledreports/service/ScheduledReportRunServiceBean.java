package de.diedavids.cuba.scheduledreports.service;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Events;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.reports.app.service.ReportService;
import com.haulmont.reports.entity.Report;
import de.diedavids.cuba.scheduledreports.core.ScheduledReportRepository;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportConfiguration;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution;
import de.diedavids.cuba.scheduledreports.events.ScheduledReportRun;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@Service(ScheduledReportRunService.NAME)
public class ScheduledReportRunServiceBean implements ScheduledReportRunService {


    @Inject
    protected ReportService reportService;
    @Inject
    protected ScheduledReportRepository scheduledReportRepository;
    @Inject
    protected DataManager dataManager;
    @Inject
    protected TimeSource timeSource;
    @Inject
    protected Events events;


    @Override
    public void runScheduledReport(String code) {

        ScheduledReportConfiguration config = scheduledReportRepository.loadByCode(code);

        runReport(config);
    }

    private void runReport(ScheduledReportConfiguration scheduledReportConfiguration) {

        Report report = scheduledReportConfiguration.getReport();

        ScheduledReportExecution scheduledReportExecution = dataManager.create(ScheduledReportExecution.class);

        FileDescriptor savedReport = null;
        try{
            savedReport = reportService.createAndSaveReport(report, Collections.emptyMap(), "my-file");
            scheduledReportExecution.setReportFile(savedReport);
            scheduledReportExecution.setSuccessful(true);
        }
        catch (Exception e) {
            scheduledReportExecution.setSuccessful(false);
        }

        scheduledReportExecution.setConfig(scheduledReportConfiguration);
        scheduledReportExecution.setExecutedAt(timeSource.currentTimestamp());

        dataManager.commit(scheduledReportExecution);

        events.publish(new ScheduledReportRun(this, savedReport, scheduledReportExecution));
    }
}