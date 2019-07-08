package de.diedavids.cuba.scheduledreports.web.screens.scheduledreportconfiguration;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.relatedentities.RelatedEntitiesAPI;
import com.haulmont.cuba.gui.relatedentities.RelatedEntitiesBuilder;
import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportConfiguration;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution;
import de.diedavids.cuba.scheduledreports.web.screens.scheduledreportexecution.ScheduledReportExecutionBrowse;

import javax.inject.Inject;

@UiController("ddcsr_ScheduledReportConfiguration.browse")
@UiDescriptor("scheduled-report-configuration-browse.xml")
@LookupComponent("scheduledReportConfigurationsTable")
@LoadDataBeforeShow
public class ScheduledReportConfigurationBrowse extends StandardLookup<ScheduledReportConfiguration> {


    @Inject
    protected RelatedEntitiesAPI relatedEntitiesAPI;
    @Inject
    protected MessageBundle messageBundle;
    @Inject
    protected GroupTable<ScheduledReportConfiguration> scheduledReportConfigurationsTable;

    @Subscribe("scheduledReportConfigurationsTable.executions")
    protected void onScheduledReportConfigurationsTableExecutions(Action.ActionPerformedEvent event) {

        relatedEntitiesAPI.builder(this)
                .withEntityClass(ScheduledReportConfiguration.class)
                .withProperty("executions")
                .withSelectedEntities(scheduledReportConfigurationsTable.getSelected())
                .withScreenClass(ScheduledReportExecutionBrowse.class)
                .withFilterCaption(messageBundle.getMessage("executionsForScheduledReport"))
                .withOpenMode(OpenMode.NEW_TAB)
                .build()
                .show();
    }


    
}