package de.diedavids.cuba.scheduledreports.web.screens.scheduledreportexecution;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution;

@UiController("ddcsr_ScheduledReportExecution.edit")
@UiDescriptor("scheduled-report-execution-edit.xml")
@EditedEntityContainer("scheduledReportExecutionDc")
@LoadDataBeforeShow
public class ScheduledReportExecutionEdit extends StandardEditor<ScheduledReportExecution> {
}