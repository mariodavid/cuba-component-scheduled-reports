package de.diedavids.cuba.scheduledreports.web.screens.scheduledreportconfiguration;

import com.haulmont.cuba.gui.screen.*;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportConfiguration;

@UiController("ddcsr_ScheduledReportConfiguration.browse")
@UiDescriptor("scheduled-report-configuration-browse.xml")
@LookupComponent("scheduledReportConfigurationsTable")
@LoadDataBeforeShow
public class ScheduledReportConfigurationBrowse extends StandardLookup<ScheduledReportConfiguration> {
}