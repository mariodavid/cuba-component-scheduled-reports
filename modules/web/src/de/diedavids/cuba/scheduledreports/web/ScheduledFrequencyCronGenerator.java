package de.diedavids.cuba.scheduledreports.web;

import com.cronutils.builder.CronBuilder;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinitionBuilder;
import de.diedavids.cuba.scheduledreports.entity.ScheduledFrequency;
import org.springframework.stereotype.Component;

import static com.cronutils.builder.CronBuilder.cron;
import static com.cronutils.model.CronType.QUARTZ;
import static com.cronutils.model.CronType.SPRING;
import static com.cronutils.model.field.expression.FieldExpressionFactory.*;
import static com.cronutils.model.field.expression.FieldExpressionFactory.on;

@Component("ddcsr_ScheduledFrequencyCronGenerator")
public class ScheduledFrequencyCronGenerator {


    public String createCronExpression(ScheduledFrequency frequency) {

        switch (frequency.getFrequency()) {

            case HOURLY:
                return "0 " + frequency.getHourlyMinute() + " * * * *";
            case DAILY:
                return "0 " + frequency.getDailyMinute() + " " + frequency.getDailyHour()  + " * * *";
            case MONTHLY:
                return "0 " + frequency.getMonthlyMinute() + " " + frequency.getMonthlyHour()  + " " + frequency.getMonthlyDay() + " * *";
        }
        return frequency.getCustomCron();

    }
}
