package de.diedavids.cuba.scheduledreports.entity;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Table(name = "DDCSR_SCHEDULED_REPORT_EXEC")
@Entity(name = "ddcsr_ScheduledReportExecution")
public class ScheduledReportExecution extends StandardEntity {
    private static final long serialVersionUID = -7581545743493852289L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CONFIG_ID")
    protected ScheduledReportConfiguration config;

    @Column(name = "SUCCESSFUL_")
    protected Boolean successful;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "EXECUTED_AT", nullable = false)
    protected Date executedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_FILE_ID")
    protected FileDescriptor reportFile;

    public void setExecutedAt(Date executedAt) {
        this.executedAt = executedAt;
    }

    public Date getExecutedAt() {
        return executedAt;
    }

    public FileDescriptor getReportFile() {
        return reportFile;
    }

    public void setReportFile(FileDescriptor reportFile) {
        this.reportFile = reportFile;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public ScheduledReportConfiguration getConfig() {
        return config;
    }

    public void setConfig(ScheduledReportConfiguration config) {
        this.config = config;
    }
}