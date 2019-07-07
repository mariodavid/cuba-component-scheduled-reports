package de.diedavids.cuba.scheduledreports.service;

public interface ScheduledReportRunService {
    String NAME = "ddcsr_ScheduledReportRunService";

    void runScheduledReport(String code);
}