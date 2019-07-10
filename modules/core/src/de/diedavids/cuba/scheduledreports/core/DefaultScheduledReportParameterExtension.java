package de.diedavids.cuba.scheduledreports.core;

import de.diedavids.cuba.scheduledreports.ScheduledReportExtension;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport;

import java.util.Map;

public class DefaultScheduledReportParameterExtension implements ScheduledReportExtension {

    @Override
    public boolean supports(ScheduledReport scheduledReport) {
        return false;
    }

    @Override
    public Map<String, Object> provideParameters(ScheduledReport scheduledReport) {
        return null;
    }

    @Override
    public String provideFilename(ScheduledReport scheduledReport) {
        return null;
    }

    @Override
    public boolean shouldBeExecuted(ScheduledReport scheduledReport) {
        return false;
    }
}
