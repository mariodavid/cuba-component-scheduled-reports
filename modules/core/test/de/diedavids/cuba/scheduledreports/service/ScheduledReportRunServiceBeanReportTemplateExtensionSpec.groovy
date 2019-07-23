package de.diedavids.cuba.scheduledreports.service


import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.Events
import com.haulmont.cuba.core.global.TimeSource
import com.haulmont.reports.ReportingApi
import com.haulmont.reports.entity.Report
import com.haulmont.reports.entity.ReportTemplate
import de.diedavids.cuba.scheduledreports.ScheduledReportExtension
import de.diedavids.cuba.scheduledreports.ScheduledReportExtensionFactory
import de.diedavids.cuba.scheduledreports.core.DefaultScheduledReportParameterExtension
import de.diedavids.cuba.scheduledreports.core.ScheduledReportRepository
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution
import spock.lang.Specification

class ScheduledReportRunServiceBeanReportTemplateExtensionSpec extends Specification {
    private ScheduledReportExtensionFactory scheduledReportExtensionFactory
    private ScheduledReportRepository scheduledReportRepository
    private ReportingApi reportingApi
    private ScheduledReportRunServiceBean sut
    private DataManager dataManager


    def setup() {
        scheduledReportRepository = Mock(ScheduledReportRepository)
        scheduledReportExtensionFactory = Mock(ScheduledReportExtensionFactory)
        reportingApi = Mock(ReportingApi)

        dataManager = Mock(DataManager)

        dataManager.create(ScheduledReportExecution) >> new ScheduledReportExecution()

        sut = new ScheduledReportRunServiceBean(
                reportingApi: reportingApi,
                scheduledReportRepository: scheduledReportRepository,
                dataManager: dataManager,
                scheduledReportExtensionFactory: scheduledReportExtensionFactory,
                timeSource: Mock(TimeSource),
                events: Mock(Events)
        )
    }

    def "runScheduledReport uses the default template of the report when the scheduled report does not provide any"() {
        given:
        def defaultReportTemplate = new ReportTemplate()

        def report = new Report(
                defaultTemplate: defaultReportTemplate
        )

        def scheduledReport = scheduledReportFor(report)

        and:
        scheduledReportIsFound(scheduledReport)

        and:
        defaultExtensionIsUsed()

        when:
        runScheduledReport(scheduledReport)

        then:
        1 * reportingApi.createAndSaveReport(report, defaultReportTemplate,_,_)
    }

    private runScheduledReport(ScheduledReport scheduledReport) {
        sut.runScheduledReport(scheduledReport.id.toString())
    }

    private ScheduledReport scheduledReportFor(Report report) {
        new ScheduledReport(
                id: UUID.randomUUID(),
                report: report
        )
    }

    def "runScheduledReport uses the configured UI report template from the scheduled report over the reports default if provided"() {
        given:
        def defaultReportTemplate = new ReportTemplate()

        def report = new Report(
                defaultTemplate: defaultReportTemplate
        )

        def scheduledReportReportTemplate = new ReportTemplate()

        def scheduledReport = scheduledReportFor(report)
        scheduledReport.reportTemplate = scheduledReportReportTemplate

        and:
        scheduledReportIsFound(scheduledReport)

        and:
        defaultExtensionIsUsed()

        when:
        runScheduledReport(scheduledReport)

        then:
        1 * reportingApi.createAndSaveReport(report, scheduledReportReportTemplate,_,_)
    }


    def "runScheduledReport uses the extension defined report template over all other options if provided"() {
        given:
        def defaultReportTemplate = new ReportTemplate()

        def report = new Report(
                defaultTemplate: defaultReportTemplate
        )

        def scheduledReportReportTemplate = new ReportTemplate()

        def scheduledReport = scheduledReportFor(report)
        scheduledReport.reportTemplate = scheduledReportReportTemplate

        and:
        scheduledReportIsFound(scheduledReport)

        and:
        def extensionReportTemplate = new ReportTemplate()

        extensionIsUsed(new TestScheduledReportExtension(
                execution: true,
                supports: true,
                reportTemplate: extensionReportTemplate,
                filename: "my-file"
        ))

        when:
        runScheduledReport(scheduledReport)

        then:
        1 * reportingApi.createAndSaveReport(report, extensionReportTemplate,_,_)
    }

    private void defaultExtensionIsUsed() {
        extensionIsUsed(new DefaultScheduledReportParameterExtension())
    }

    private void extensionIsUsed(ScheduledReportExtension scheduledReportExtension) {
        scheduledReportExtensionFactory.create(_) >> scheduledReportExtension
    }

    private void scheduledReportIsFound(ScheduledReport scheduledReport) {
        scheduledReportRepository.loadById(scheduledReport.id, _) >> Optional.of(scheduledReport)
    }

}


class TestScheduledReportExtension implements ScheduledReportExtension {

    Map<String, Object> parameters
    boolean supports
    String filename
    boolean execution
    ReportTemplate reportTemplate

    @Override
    boolean supports(ScheduledReport scheduledReport) {
        return supports
    }

    @Override
    Map<String, Object> provideParameters(ScheduledReport scheduledReport) {
        return parameters
    }

    @Override
    Optional<String> provideFilename(ScheduledReport scheduledReport, ReportTemplate reportTemplate) {
        return Optional.ofNullable(filename)
    }

    @Override
    boolean shouldBeExecuted(ScheduledReport scheduledReport) {
        return execution
    }

    @Override
    Optional<ReportTemplate> provideReportTemplate(ScheduledReport scheduledReport, Report report) {
        return Optional.ofNullable(reportTemplate)
    }
}