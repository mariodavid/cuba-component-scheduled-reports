package de.diedavids.cuba.scheduledreports.core;

import com.haulmont.cuba.core.global.DataManager;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportConfiguration;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component(ScheduledReportRepository.NAME)
public class ScheduledReportRepository {
    public static final String NAME = "ddcsr_ScheduledReportRepository";

    @Inject
    protected DataManager dataManager;


    public List<ScheduledReportConfiguration> loadActiveConfigurations() {
        return dataManager.load(ScheduledReportConfiguration.class)
                .query("select e from ddcsr_ScheduledReportConfiguration e where e.active = true")
                .view("configuration-with-executions")
                .list();
    };

    public ScheduledReportConfiguration loadByCode(String code) {
        return dataManager.load(ScheduledReportConfiguration.class)
                .query("select e from ddcsr_ScheduledReportConfiguration e where e.code = :code")
                .parameter("code", code)
                .view("configuration-with-executions")
                .one();
    }
}