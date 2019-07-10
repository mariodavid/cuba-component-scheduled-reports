package de.diedavids.cuba.scheduledreports.core;

import com.haulmont.reports.entity.Report;
import com.haulmont.reports.entity.ReportTemplate;
import de.diedavids.cuba.scheduledreports.ScheduledReportExtension;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class DefaultScheduledReportParameterExtension implements ScheduledReportExtension {

    @Override
    public boolean supports(ScheduledReport scheduledReport) {
        return true;
    }

    @Override
    public Map<String, Object> provideParameters(ScheduledReport scheduledReport) {
        return Collections.emptyMap();
    }

    @Override
    public Optional<String> provideFilename(ScheduledReport scheduledReport, ReportTemplate reportTemplate) {
        return Optional.ofNullable(reportTemplate.getOutputNamePattern());
    }


    @Override
    public boolean shouldBeExecuted(ScheduledReport scheduledReport) {
        return true;
    }

    @Override
    public Optional<ReportTemplate> provideReportTemplate(ScheduledReport scheduledReport, Report report) {
        ReportTemplate selectedReportTemplate = scheduledReport.getReportTemplate();

        Optional<ReportTemplate> optionalSelectedReportTemplate = Optional.ofNullable(selectedReportTemplate);

        if (optionalSelectedReportTemplate.isPresent()) {
            return optionalSelectedReportTemplate;
        }

        return Optional.ofNullable(report.getDefaultTemplate());
    }

}
