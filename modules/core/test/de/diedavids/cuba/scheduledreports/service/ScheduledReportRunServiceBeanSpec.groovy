package de.diedavids.cuba.scheduledreports.service

import com.haulmont.addon.emailtemplates.entity.JsonEmailTemplate
import com.haulmont.cuba.core.entity.FileDescriptor
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.Events
import com.haulmont.cuba.core.global.TimeSource
import com.haulmont.reports.ReportingApi
import com.haulmont.reports.entity.Report
import com.haulmont.reports.entity.ReportTemplate
import de.diedavids.cuba.scheduledreports.ScheduledReportExtensionFactory
import de.diedavids.cuba.scheduledreports.core.DefaultScheduledReportParameterExtension
import de.diedavids.cuba.scheduledreports.core.ScheduledReportEmailing
import de.diedavids.cuba.scheduledreports.core.ScheduledReportRepository
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution
import de.diedavids.cuba.scheduledreports.events.ScheduledReportRun
import spock.lang.Specification

class ScheduledReportRunServiceBeanSpec extends Specification {
    private ScheduledReportExtensionFactory scheduledReportExtensionFactory
    private ScheduledReportRepository scheduledReportRepository
    private ReportingApi reportingApi
    private ScheduledReportRunServiceBean sut
    private DataManager dataManager
    private Events events
    private ScheduledReportEmailing scheduledReportEmailing
    private ScheduledReportExecution scheduledReportExecution


    def setup() {
        scheduledReportRepository = Mock(ScheduledReportRepository)
        scheduledReportExtensionFactory = Mock(ScheduledReportExtensionFactory)
        reportingApi = Mock(ReportingApi)

        dataManager = Mock(DataManager)

        scheduledReportExecution = new ScheduledReportExecution()
        dataManager.create(ScheduledReportExecution) >> scheduledReportExecution
        dataManager.commit(scheduledReportExecution) >> scheduledReportExecution

        events = Mock(Events)
        scheduledReportEmailing = Mock(ScheduledReportEmailing)
        sut = new ScheduledReportRunServiceBean(
                reportingApi: reportingApi,
                scheduledReportRepository: scheduledReportRepository,
                dataManager: dataManager,
                scheduledReportExtensionFactory: scheduledReportExtensionFactory,
                timeSource: Mock(TimeSource),
                events: events,
                scheduledReportEmailing: scheduledReportEmailing
        )
    }

    def "runScheduledReport fetches the report by code and runs the report"() {
        given:
        def report = new Report(
                defaultTemplate: new ReportTemplate()
        )

        def scheduledReport = new ScheduledReport(
                report: report
        )

        and:
        scheduledReportRepository.loadByCode('my_report', _) >> scheduledReport

        and:
        scheduledReportExtensionFactory.create(scheduledReport) >> new DefaultScheduledReportParameterExtension()

        when:
        sut.runScheduledReport('my_report')

        then:
        1 * reportingApi.createAndSaveReport(report, _,_,_)
    }

    def "runScheduledReport stores the created file descriptor in the execution instance"() {

        given:

        def report = new Report(
                defaultTemplate: new ReportTemplate()
        )

        def scheduledReport = new ScheduledReport(
                report: report
        )

        and:
        scheduledReportRepository.loadByCode('my_report', _) >> scheduledReport

        and:
        scheduledReportExtensionFactory.create(scheduledReport) >> new DefaultScheduledReportParameterExtension()

        def storedFileDescriptor = new FileDescriptor()

        and:
        reportingApi.createAndSaveReport(report, _,_,_) >> storedFileDescriptor

        when:
        sut.runScheduledReport('my_report')

        then:
        1 * dataManager.commit({ ScheduledReportExecution execution ->
            execution.reportFile == storedFileDescriptor
        })
    }

    def "runScheduledReport sends out a notification once report was run"() {

        given:

        def report = new Report(
                defaultTemplate: new ReportTemplate()
        )

        def scheduledReport = new ScheduledReport(
                report: report
        )

        and:
        scheduledReportRepository.loadByCode('my_report', _) >> scheduledReport

        and:
        scheduledReportExtensionFactory.create(scheduledReport) >> new DefaultScheduledReportParameterExtension()

        def storedFileDescriptor = new FileDescriptor()

        and:
        reportingApi.createAndSaveReport(report, _,_,_) >> storedFileDescriptor

        when:
        sut.runScheduledReport('my_report')

        then:
        1 * events.publish({ ScheduledReportRun event ->

            event.reportFile == storedFileDescriptor &&
            event.reportExecution.successful == true
        })
    }
    def "runScheduledReport sends out a notification even if execution failed"() {

        given:

        def report = new Report(
                defaultTemplate: new ReportTemplate()
        )

        def scheduledReport = new ScheduledReport(
                report: report
        )

        and:
        scheduledReportRepository.loadByCode('my_report', _) >> scheduledReport

        and:
        scheduledReportExtensionFactory.create(scheduledReport) >> new DefaultScheduledReportParameterExtension()

        and:
        reportingApi.createAndSaveReport(report, _,_,_) >> {throw new RuntimeException("did not work")}

        when:
        sut.runScheduledReport('my_report')

        then:
        1 * events.publish({ ScheduledReportRun event ->

            event.reportFile == null &&
            event.reportExecution.successful == false
        })
    }
    def "runScheduledReport triggers email sending for a scheduled report with activated email"() {

        given:

        def report = new Report(
                defaultTemplate: new ReportTemplate(),
        )

        def emailTemplate = new JsonEmailTemplate()
        def scheduledReport = new ScheduledReport(
                report: report,
                emailTemplate: emailTemplate,
                sendEmail: true
        )

        and:
        scheduledReportRepository.loadByCode('my_report', _) >> scheduledReport

        and:
        scheduledReportExtensionFactory.create(scheduledReport) >> new DefaultScheduledReportParameterExtension()

        and:
        reportingApi.createAndSaveReport(report, _,_,_) >> Mock(FileDescriptor)

        when:
        sut.runScheduledReport('my_report')

        then:
        1 * scheduledReportEmailing.sendEmailForScheduledReport(emailTemplate, scheduledReportExecution)
    }
}
