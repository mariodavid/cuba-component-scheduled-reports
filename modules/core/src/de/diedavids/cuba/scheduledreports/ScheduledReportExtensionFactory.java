package de.diedavids.cuba.scheduledreports;

import com.haulmont.cuba.core.global.BeanLocator;
import de.diedavids.cuba.scheduledreports.core.DefaultScheduledReportParameterExtension;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport;
import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("ddcsr_LauncherCommandExecutorFactory")
public class ScheduledReportExtensionFactory {

    @Inject
    private BeanLocator beanLocator;

    public ScheduledReportExtension create(ScheduledReport scheduledReport) {
        return findBeanExtension(scheduledReport);
    }

    public ScheduledReportExtension findBeanExtension(ScheduledReport scheduledReport) {
        Map<String, ScheduledReportExtension> potentialExtensions = beanLocator.getAll(ScheduledReportExtension.class);

        List<ScheduledReportExtension> matchingExtensions = potentialExtensions.entrySet().stream()
                .filter(kv -> kv.getValue().supports(scheduledReport))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        if (matchingExtensions.size() > 1) {
            throw new MultipleScheduledReportExtensionsFound();
        }
        if (matchingExtensions.size() == 0) {
            return new DefaultScheduledReportParameterExtension();
        }

        return matchingExtensions.get(0);
    }

}
