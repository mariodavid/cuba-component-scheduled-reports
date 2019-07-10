package de.diedavids.cuba.scheduledreports.core;

import com.haulmont.cuba.core.global.DataManager;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(ScheduledReportRepository.NAME)
public class ScheduledReportRepository {
    public static final String NAME = "ddcsr_ScheduledReportRepository";

    @Inject
    protected DataManager dataManager;

    public ScheduledReport loadByCode(String code, String view) {
        return dataManager.load(ScheduledReport.class)
                .query("select e from ddcsr_ScheduledReport e where e.code = :code")
                .parameter("code", code)
                .view(view)
                .one();
    }
}