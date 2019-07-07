package de.diedavids.cuba.scheduledreports.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.cuba.core.entity.EmbeddableEntity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@MetaClass(name = "ddcsr_ScheduledFrequency")
public class ScheduledFrequency extends EmbeddableEntity {
    private static final long serialVersionUID = -7672870434698799710L;

    @NotNull
    @Column(name = "FREQUENCY", nullable = false)
    protected String frequency;

    @Column(name = "CUSTOM_CRON")
    protected String customCron;

    @Column(name = "DAILY_HOUR")
    protected Integer dailyHour;

    @Column(name = "DAILY_MINUTE")
    protected Integer dailyMinute;

    @Column(name = "HOURLY_MINUTE")
    protected Integer hourlyMinute;

    @Column(name = "MONTHLY_DAY")
    protected Integer monthlyDay;

    @Column(name = "MONTHLY_HOUR")
    protected Integer monthlyHour;

    @Column(name = "MONTHLY_MINUTE")
    protected Integer monthlyMinute;

    public Integer getMonthlyMinute() {
        return monthlyMinute;
    }

    public void setMonthlyMinute(Integer monthlyMinute) {
        this.monthlyMinute = monthlyMinute;
    }

    public Integer getMonthlyHour() {
        return monthlyHour;
    }

    public void setMonthlyHour(Integer monthlyHour) {
        this.monthlyHour = monthlyHour;
    }

    public Integer getMonthlyDay() {
        return monthlyDay;
    }

    public void setMonthlyDay(Integer monthlyDay) {
        this.monthlyDay = monthlyDay;
    }

    public Integer getHourlyMinute() {
        return hourlyMinute;
    }

    public void setHourlyMinute(Integer hourlyMinute) {
        this.hourlyMinute = hourlyMinute;
    }

    public Integer getDailyMinute() {
        return dailyMinute;
    }

    public void setDailyMinute(Integer dailyMinute) {
        this.dailyMinute = dailyMinute;
    }

    public Integer getDailyHour() {
        return dailyHour;
    }

    public void setDailyHour(Integer dailyHour) {
        this.dailyHour = dailyHour;
    }

    public String getCustomCron() {
        return customCron;
    }

    public void setCustomCron(String customCron) {
        this.customCron = customCron;
    }

    public ScheduledFrequencyType getFrequency() {
        return frequency == null ? null : ScheduledFrequencyType.fromId(frequency);
    }

    public void setFrequency(ScheduledFrequencyType frequency) {
        this.frequency = frequency == null ? null : frequency.getId();
    }
}