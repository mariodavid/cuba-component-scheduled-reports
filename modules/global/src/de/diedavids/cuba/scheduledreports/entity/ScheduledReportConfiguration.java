package de.diedavids.cuba.scheduledreports.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.EmbeddedParameters;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.reports.entity.Report;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@NamePattern("%s|name")
@Table(name = "DDCSR_SCHEDULED_REPORT_CONFIG")
@Entity(name = "ddcsr_ScheduledReportConfiguration")
public class ScheduledReportConfiguration extends StandardEntity {
    private static final long serialVersionUID = 867426585502431134L;


    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REPORT_ID")
    protected Report report;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SCHEDULED_TASK_ID")
    protected ScheduledTask scheduledTask;

    @NotNull
    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "CODE")
    protected String code;

    @Column(name = "ACTIVE")
    protected Boolean active;

    @Embedded
    @EmbeddedParameters(nullAllowed = false)
    @AttributeOverrides({
            @AttributeOverride(name = "frequency", column = @Column(name = "FREQUENCY_FREQUENCY")),
            @AttributeOverride(name = "customCron", column = @Column(name = "FREQUENCY_CUSTOM_CRON")),
            @AttributeOverride(name = "dailyHour", column = @Column(name = "FREQUENCY_DAILY_HOUR")),
            @AttributeOverride(name = "dailyMinute", column = @Column(name = "FREQUENCY_DAILY_MINUTE")),
            @AttributeOverride(name = "hourlyMinute", column = @Column(name = "FREQUENCY_HOURLY_MINUTE")),
            @AttributeOverride(name = "monthlyDay", column = @Column(name = "FREQUENCY_MONTHLY_DAY")),
            @AttributeOverride(name = "monthlyHour", column = @Column(name = "FREQUENCY_MONTHLY_HOUR"))
    })
    protected ScheduledFrequency frequency;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "config")
    protected List<ScheduledReportExecution> executions;

    public ScheduledFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(ScheduledFrequency frequency) {
        this.frequency = frequency;
    }

    public List<ScheduledReportExecution> getExecutions() {
        return executions;
    }

    public void setExecutions(List<ScheduledReportExecution> executions) {
        this.executions = executions;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public ScheduledTask getScheduledTask() {
        return scheduledTask;
    }

    public void setScheduledTask(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }
}