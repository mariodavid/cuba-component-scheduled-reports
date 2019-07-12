package de.diedavids.cuba.scheduledreports.entity;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Table(name = "DDCSR_SCHEDULED_REPORT_EXEC")
@Entity(name = "ddcsr_ScheduledReportExecution")
public class ScheduledReportExecution extends StandardEntity {
    private static final long serialVersionUID = -7581545743493852289L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SCHEDULED_REPORT_ID")
    protected ScheduledReport scheduledReport;

    @Column(name = "SUCCESSFUL_")
    protected Boolean successful;

    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "EXECUTED_AT", nullable = false)
    protected Date executedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REPORT_FILE_ID")
    protected FileDescriptor reportFile;

    @JoinTable(name = "DDCSR_SR_EXEC_MESSAGE_LINK", joinColumns = @JoinColumn(name = "SCHEDULED_REPORT_EXECUTION_ID"), inverseJoinColumns = @JoinColumn(name = "SENDING_MESSAGE_ID"))
    @ManyToMany
    protected List<SendingMessage> sendingMessages;


    public List<SendingMessage> getSendingMessages() {
        return sendingMessages;
    }

    public void setSendingMessages(List<SendingMessage> sendingMessages) {
        this.sendingMessages = sendingMessages;
    }


    public ScheduledReport getScheduledReport() {
        return scheduledReport;
    }

    public void setScheduledReport(ScheduledReport scheduledReport) {
        this.scheduledReport = scheduledReport;
    }

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

}