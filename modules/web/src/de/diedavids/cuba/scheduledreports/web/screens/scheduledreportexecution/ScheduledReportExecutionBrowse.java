package de.diedavids.cuba.scheduledreports.web.screens.scheduledreportexecution;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution;

@UiController("ddcsr_ScheduledReportExecution.browse")
@UiDescriptor("scheduled-report-execution-browse.xml")
@LookupComponent("scheduledReportExecutionsTable")
@LoadDataBeforeShow
public class ScheduledReportExecutionBrowse extends StandardLookup<ScheduledReportExecution> {
}