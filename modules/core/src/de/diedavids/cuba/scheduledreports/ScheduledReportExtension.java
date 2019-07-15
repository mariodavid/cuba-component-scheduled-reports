package de.diedavids.cuba.scheduledreports;

import com.haulmont.reports.entity.Report;
import com.haulmont.reports.entity.ReportTemplate;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport;

import java.util.Map;
import java.util.Optional;


/**
 * A programmatic extension of the execution of a particular scheduled report.
 *
 * It is called for every execution of a scheduled report and can for each execution decide
 * which parameter values to set, which report template should be used and if the execution should take place.
 *
 * Implementations should be an Spring component
 */
public interface ScheduledReportExtension  {


    /**
     * defines if a given scheduled report extension supports a particular scheduled report
     *
     * @param scheduledReport the scheduled report to run
     * @return true if the extension should be used for this scheduled report, otherwise false
     */
    boolean supports(ScheduledReport scheduledReport);


    /**
     * hook method to provide the report parameters as a Map. The keys have to match the names of the parameters
     * defined in the report.
     *
     * @param scheduledReport the scheduled report to run
     * @return filled parameter map that is passed to the report engine
     */
    Map<String, Object> provideParameters(ScheduledReport scheduledReport);


    /**
     * hook method to provide the report template, that should be used for the scheduled report execution.
     *
     * As there are multiple places to define the report template that should be used, he following hierarchy for
     * selecting is used (sort by descending priority):
     *
     * 1. the selection of this method implementation
     * 2. the statically selected report template of the scheduled report instance
     * 3. the default report template of the report instance
     *
     * @param scheduledReport the scheduled report to run
     * @param report the report instance to run
     * @return an optional report template that overrides the above mentioned hierarchy
     */
    Optional<ReportTemplate> provideReportTemplate(ScheduledReport scheduledReport, Report report);


    /**
     * hook method to provide the target filename of the report file
     *
     * @param scheduledReport the scheduled report to run
     * @param reportTemplate the chosen report template
     * @return an optional String that defines the file of the report file
     */
    Optional<String> provideFilename(ScheduledReport scheduledReport, ReportTemplate reportTemplate);


    /**
     * hook method which acts as a veto option that allows to define if a particular scheduled report instance
     * should be run or not. This gives the ability to programmatically define that a report execution should be skipped,
     * because of some particular context (weekends, associated data is not available etc.)
     *
     * @param scheduledReport the scheduled report to run
     * @return true if the scheduled report instance for this schedule should be executed, otherwise false
     */
    boolean shouldBeExecuted(ScheduledReport scheduledReport);

}
