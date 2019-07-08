package de.diedavids.cuba.scheduledreports.web.screens.scheduledreportconfiguration;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.field.value.SpecialChar;
import com.cronutils.parser.CronParser;
import com.google.common.collect.Lists;
import com.haulmont.cuba.core.app.scheduled.MethodParameterInfo;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.entity.ScheduledTaskDefinedBy;
import com.haulmont.cuba.core.entity.SchedulingType;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.HBoxLayout;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.model.DataContext;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.entity.ReportTemplate;
import de.diedavids.cuba.scheduledreports.entity.ScheduledFrequency;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportConfiguration;
import de.diedavids.cuba.scheduledreports.entity.ScheduledFrequencyType;
import de.diedavids.cuba.scheduledreports.web.ScheduledFrequencyCronGenerator;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cronutils.builder.CronBuilder.cron;
import static com.cronutils.model.CronType.QUARTZ;

import static com.cronutils.model.CronType.SPRING;
import static com.cronutils.model.field.expression.FieldExpressionFactory.*;
import static java.util.stream.IntStream.range;

@UiController("ddcsr_ScheduledReportConfiguration.edit")
@UiDescriptor("scheduled-report-configuration-edit.xml")
@EditedEntityContainer("scheduledReportConfigurationDc")
@LoadDataBeforeShow
public class ScheduledReportConfigurationEdit extends StandardEditor<ScheduledReportConfiguration> {


    @Inject
    protected DataContext dataContext;

    @Inject
    protected LookupField<Integer> dailyHourField;
    @Inject
    protected LookupField<Integer> dailyMinuteField;
    @Inject
    protected LookupField<Integer> hourlyMinuteField;
    @Inject
    protected LookupField<Integer> monthlyDayField;
    @Inject
    protected LookupField<Integer> monthlyHourField;
    @Inject
    protected LookupField<Integer> monthlyMinuteField;


    @Inject
    protected HBoxLayout dailyTimeFields;
    @Inject
    protected HBoxLayout hourlyTimeFields;
    @Inject
    protected HBoxLayout monthlyTimeFields;
    @Inject
    protected HBoxLayout monthlyDayFields;
    @Inject
    protected HBoxLayout customFields;
    @Inject
    protected ScheduledFrequencyCronGenerator scheduledFrequencyCronGenerator;
    @Inject
    protected LookupField<ReportTemplate> reportTemplateLookupField;

    @Subscribe("reportField")
    protected void onReportFieldValueChange(HasValue.ValueChangeEvent<Report> event) {
        List<ReportTemplate> templates = getEditedEntity().getReport().getTemplates();
        reportTemplateLookupField.setOptionsList(templates);
    }


    @Subscribe("frequencyTypeField")
    protected void onFrequencyOptionsGroupValueChange(HasValue.ValueChangeEvent event) {

        ScheduledFrequencyType value = (ScheduledFrequencyType) event.getValue();
        if (value.equals(ScheduledFrequencyType.DAILY)) {
            dailyTimeFields.setVisible(true);
            hourlyTimeFields.setVisible(false);
            monthlyTimeFields.setVisible(false);
            monthlyDayFields.setVisible(false);
            customFields.setVisible(false);
        }
        else if (value.equals(ScheduledFrequencyType.HOURLY)){
            dailyTimeFields.setVisible(false);
            hourlyTimeFields.setVisible(true);
            monthlyTimeFields.setVisible(false);
            monthlyDayFields.setVisible(false);
            customFields.setVisible(false);
        }
        else if (value.equals(ScheduledFrequencyType.MONTHLY)){
            dailyTimeFields.setVisible(false);
            hourlyTimeFields.setVisible(false);
            monthlyTimeFields.setVisible(true);
            monthlyDayFields.setVisible(true);
            customFields.setVisible(false);
        }
        else if (value.equals(ScheduledFrequencyType.CUSTOM)){
            dailyTimeFields.setVisible(false);
            hourlyTimeFields.setVisible(false);
            monthlyTimeFields.setVisible(false);
            monthlyDayFields.setVisible(false);
            customFields.setVisible(true);
        }
    }




    @Subscribe
    protected void onAfterInit(AfterInitEvent event) {

        dailyHourField.setOptionsList(intRange(0, 23));
        dailyMinuteField.setOptionsList(intRange(0, 59));

        hourlyMinuteField.setOptionsList(intRange(0, 59));

        monthlyHourField.setOptionsList(intRange(0, 23));
        monthlyMinuteField.setOptionsList(intRange(0, 59));
        monthlyDayField.setOptionsList(intRange(1, 31));

    }

    private List<Integer> intRange(int from, int to) {
        return range(from, to + 1).boxed().collect(Collectors.toList());
    }


    @Subscribe
    protected void onBeforeCommitChanges(BeforeCommitChangesEvent event) {


        ScheduledReportConfiguration config = getEditedEntity();

        String cronExpression = getCronExpression(config);
        ScheduledTask scheduledTask = config.getScheduledTask() == null ? createOne() : config.getScheduledTask();

        scheduledTask.setActive(config.getActive());
        scheduledTask.setDefinedBy(ScheduledTaskDefinedBy.BEAN);
        scheduledTask.setBeanName("ddcsr_ScheduledReportRunService");
        scheduledTask.setMethodName("runScheduledReport");

        ArrayList<MethodParameterInfo> parameter = Lists.newArrayList(
                new MethodParameterInfo("java.lang.String", "code", config.getCode())
        );
        scheduledTask.updateMethodParameters(parameter);
        scheduledTask.setCron(cronExpression);
        scheduledTask.setLogStart(true);
        scheduledTask.setLogFinish(true);
        scheduledTask.setSchedulingType(SchedulingType.CRON);
        scheduledTask.setDescription("Scheduled Report: " + getEditedEntity().getCode());
        ScheduledTask mergedScheduledTask = dataContext.merge(scheduledTask);
        getEditedEntity().setScheduledTask(mergedScheduledTask);

    }

    private String getCronExpression(ScheduledReportConfiguration config) {
        return scheduledFrequencyCronGenerator.createCronExpression(config.getFrequency());
    }


    private ScheduledTask createOne() {
        return dataContext.create(ScheduledTask.class);
    }

}