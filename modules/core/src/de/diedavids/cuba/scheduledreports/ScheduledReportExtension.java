package de.diedavids.cuba.scheduledreports;

import com.haulmont.reports.entity.Report;
import com.haulmont.reports.entity.ReportTemplate;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport;

import java.util.Map;
import java.util.Optional;

public interface ScheduledReportExtension  {

    boolean supports(ScheduledReport scheduledReport);

    Map<String, Object> provideParameters(ScheduledReport scheduledReport);

    Optional<String> provideFilename(ScheduledReport scheduledReport, ReportTemplate reportTemplate);

    boolean shouldBeExecuted(ScheduledReport scheduledReport);

    Optional<ReportTemplate> provideReportTemplate(ScheduledReport scheduledReport, Report report);
}
