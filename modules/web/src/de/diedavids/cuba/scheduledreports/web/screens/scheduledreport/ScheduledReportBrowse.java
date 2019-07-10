package de.diedavids.cuba.scheduledreports.web.screens.scheduledreport;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.relatedentities.RelatedEntitiesAPI;
import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport;
import de.diedavids.cuba.scheduledreports.web.screens.scheduledreportexecution.ScheduledReportExecutionBrowse;

import javax.inject.Inject;

@UiController("ddcsr_ScheduledReport.browse")
@UiDescriptor("scheduled-report-browse.xml")
@LookupComponent("scheduledReportsTable")
@LoadDataBeforeShow
public class ScheduledReportBrowse extends StandardLookup<ScheduledReport> {


    @Inject
    protected RelatedEntitiesAPI relatedEntitiesAPI;
    @Inject
    protected MessageBundle messageBundle;
    @Inject
    protected GroupTable<ScheduledReport> scheduledReportsTable;

    @Subscribe("scheduledReportsTable.executions")
    protected void onscheduledReportsTableExecutions(Action.ActionPerformedEvent event) {

        relatedEntitiesAPI.builder(this)
                .withEntityClass(ScheduledReport.class)
                .withProperty("executions")
                .withSelectedEntities(scheduledReportsTable.getSelected())
                .withScreenClass(ScheduledReportExecutionBrowse.class)
                .withFilterCaption(messageBundle.getMessage("executionsForScheduledReport"))
                .withOpenMode(OpenMode.NEW_TAB)
                .build()
                .show();
    }


    
}