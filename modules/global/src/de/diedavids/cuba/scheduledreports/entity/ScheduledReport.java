package de.diedavids.cuba.scheduledreports.entity;

import com.haulmont.addon.emailtemplates.entity.EmailTemplate;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.EmbeddedParameters;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.reports.entity.Report;
import com.haulmont.reports.entity.ReportTemplate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@NamePattern("%s|name")
@Table(name = "DDCSR_SCHEDULED_REPORT")
@Entity(name = "ddcsr_ScheduledReport")
public class ScheduledReport extends StandardEntity {
    private static final long serialVersionUID = 867426585502431134L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REPORT_ID")
    protected Report report;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_TEMPLATE_ID")
    protected ReportTemplate reportTemplate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SCHEDULED_TASK_ID")
    protected ScheduledTask scheduledTask;

    @NotNull
    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "CODE")
    protected String code;

    @Column(name = "DESCRIPTION", length = 4000)
    protected String description;

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
    @OneToMany(mappedBy = "scheduledReport")
    protected List<ScheduledReportExecution> executions;

    @Column(name = "SEND_EMAIL")
    protected Boolean sendEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMAIL_TEMPLATE_ID")
    protected EmailTemplate emailTemplate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public void setEmailTemplate(EmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public EmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

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


    public ReportTemplate getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(ReportTemplate reportTemplate) {
        this.reportTemplate = reportTemplate;
    }
}