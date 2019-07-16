package de.diedavids.cuba.scheduledreports.service;

public interface ScheduledReportRunService {
    String NAME = "ddcsr_ScheduledReportRunService";

    boolean isSchedulingSystemActive();

    void runScheduledReport(String code);
}