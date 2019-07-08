package de.diedavids.cuba.scheduledreports.events;

import com.haulmont.cuba.core.entity.FileDescriptor;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution;
import org.springframework.context.ApplicationEvent;

public class ScheduledReportRun extends ApplicationEvent {

    private final FileDescriptor reportFile;
    private final ScheduledReportExecution reportExecution;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ScheduledReportRun(Object source, FileDescriptor reportFile, ScheduledReportExecution reportExecution) {
        super(source);
        this.reportFile = reportFile;
        this.reportExecution = reportExecution;
    }

    public FileDescriptor getReportFile() {
        return reportFile;
    }

    public ScheduledReportExecution getReportExecution() {
        return reportExecution;
    }
}
