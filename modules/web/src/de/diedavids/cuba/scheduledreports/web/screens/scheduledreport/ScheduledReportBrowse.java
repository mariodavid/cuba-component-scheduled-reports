package de.diedavids.cuba.scheduledreports.web.screens.scheduledreport;

import com.haulmont.cuba.core.app.SchedulingService;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.relatedentities.RelatedEntitiesAPI;
import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport;
import de.diedavids.cuba.scheduledreports.service.ScheduledReportRunService;
import de.diedavids.cuba.scheduledreports.web.screens.scheduledreportexecution.ScheduledReportExecutionBrowse;

import javax.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;

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


    @Inject
    protected ScheduledReportRunService scheduledReportRunService;
    @Inject
    protected Label<String> schedulingSystemInactiveBox;

    @Subscribe
    protected void onInit(InitEvent event) {
        schedulingSystemInactiveBox.setVisible(!scheduledReportRunService.isSchedulingSystemActive());
    }


    @Subscribe("scheduledReportsTable.executions")
    protected void onscheduledReportsTableExecutions(Action.ActionPerformedEvent event) {

        Set<ScheduledReport> scheduledReports = scheduledReportsTable.getSelected();
        relatedEntitiesAPI.builder(this)
                .withEntityClass(ScheduledReport.class)
                .withProperty("executions")
                .withSelectedEntities(scheduledReports)
                .withScreenClass(ScheduledReportExecutionBrowse.class)
                .withFilterCaption(executionsFilterCaption(scheduledReports))
                .withOpenMode(OpenMode.NEW_TAB)
                .build()
                .show();
    }

    private String executionsFilterCaption(Set<ScheduledReport> scheduledReports) {
        return messageBundle.formatMessage("executionsForScheduledReport", scheduledReports.stream().map(ScheduledReport::getName).collect(Collectors.joining(", ")));
    }


}