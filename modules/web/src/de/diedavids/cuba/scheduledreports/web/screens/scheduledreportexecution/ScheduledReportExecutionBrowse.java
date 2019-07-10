package de.diedavids.cuba.scheduledreports.web.screens.scheduledreportexecution;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution;

import javax.inject.Inject;

@UiController("ddcsr_ScheduledReportExecution.browse")
@UiDescriptor("scheduled-report-execution-browse.xml")
@LookupComponent("scheduledReportExecutionsTable")
@LoadDataBeforeShow
public class ScheduledReportExecutionBrowse extends StandardLookup<ScheduledReportExecution> {


    @Inject
    protected ExportDisplay exportDisplay;

    public void downloadFile(Entity item, String columnId) {

        ScheduledReportExecution execution = (ScheduledReportExecution) item;

        exportDisplay.show(execution.getReportFile());
    }
}