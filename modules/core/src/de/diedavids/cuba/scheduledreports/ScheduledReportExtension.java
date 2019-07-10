package de.diedavids.cuba.scheduledreports;

import de.diedavids.cuba.scheduledreports.entity.ScheduledReport;

import java.util.Map;

public interface ScheduledReportExtension  {

    boolean supports(ScheduledReport scheduledReport);

    Map<String, Object> provideParameters(ScheduledReport scheduledReport);

    String provideFilename(ScheduledReport scheduledReport);

    boolean shouldBeExecuted(ScheduledReport scheduledReport);
}
