package de.diedavids.cuba.scheduledreports.service

import com.haulmont.addon.emailtemplates.builder.EmailTemplateBuilder
import com.haulmont.addon.emailtemplates.builder.EmailTemplateBuilderImpl
import com.haulmont.addon.emailtemplates.core.EmailTemplatesAPI
import com.haulmont.addon.emailtemplates.entity.JsonEmailTemplate
import com.haulmont.cuba.core.app.EmailerAPI
import com.haulmont.cuba.core.entity.FileDescriptor
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.EmailInfo
import com.haulmont.cuba.core.global.Events
import com.haulmont.cuba.core.global.TimeSource
import com.haulmont.reports.ReportingApi
import com.haulmont.reports.entity.Report
import com.haulmont.reports.entity.ReportTemplate
import de.diedavids.cuba.scheduledreports.ScheduledReportExtensionFactory
import de.diedavids.cuba.scheduledreports.core.DefaultScheduledReportParameterExtension
import de.diedavids.cuba.scheduledreports.core.ScheduledReportRepository
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution
import de.diedavids.cuba.scheduledreports.events.ScheduledReportRun
import spock.lang.Specification

import javax.validation.constraints.Email

class ScheduledReportRunServiceBeanEmailSendingSpec extends Specification {
    private ScheduledReportExtensionFactory scheduledReportExtensionFactory
    private ScheduledReportRepository scheduledReportRepository
    private ReportingApi reportingApi
    private ScheduledReportRunServiceBean sut
    private DataManager dataManager
    private Events events
    private EmailerAPI emailerAPI
    private EmailTemplatesAPI emailTemplatesAPI
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
        emailerAPI = Mock(EmailerAPI)
        emailTemplatesAPI = Mock(EmailTemplatesAPI)
        sut = new ScheduledReportRunServiceBean(
                reportingApi: reportingApi,
                scheduledReportRepository: scheduledReportRepository,
                dataManager: dataManager,
                scheduledReportExtensionFactory: scheduledReportExtensionFactory,
                timeSource: Mock(TimeSource),
                events: events,
                emailTemplatesAPI: emailTemplatesAPI,
                emailerAPI: emailerAPI
        )
    }


    def "runScheduledReport attaches the generated report file as an attachment"() {

        given:
        def report = new Report(
                defaultTemplate: new ReportTemplate()
        )

        def emailTemplate = new JsonEmailTemplate(
                to: "some@email.com",
                subject: "your report..."
        )

        def scheduledReport = new ScheduledReport(
                report: report,
                emailTemplate: emailTemplate
        )

        and:
        scheduledReportIsFound(scheduledReport)

        and:
        noExtensionIsActive(scheduledReport)

        and:
        def storedReportFile = new FileDescriptor()

        and:
        reportGenerationSucceedsWith(report, storedReportFile)

        and:
        def builder = Mock(EmailTemplateBuilder)
        emailTemplatesAPI.buildFromTemplate(_) >> builder

        when:
        sut.runScheduledReport('my_report')

        then:
        1 * builder.addAttachmentFile(storedReportFile) >> builder

    }
    def "runScheduledReport trigges Emailer API to send out the email asynchronously"() {

        given:
        def report = new Report(
                defaultTemplate: new ReportTemplate()
        )

        def emailTemplate = new JsonEmailTemplate(
                to: "some@email.com",
                subject: "your report..."
        )

        def scheduledReport = new ScheduledReport(
                report: report,
                emailTemplate: emailTemplate
        )

        and:
        scheduledReportIsFound(scheduledReport)

        and:
        noExtensionIsActive(scheduledReport)

        and:
        def storedReportFile = new FileDescriptor()

        and:
        reportGenerationSucceedsWith(report, storedReportFile)

        and:
        def emailInfo = Mock(EmailInfo)
        emailTemplatesCreatesTheEmail(emailInfo)

        when:
        sut.runScheduledReport('my_report')

        then:
        1 * emailerAPI.sendEmailAsync(emailInfo)

    }

    private void emailTemplatesCreatesTheEmail(EmailInfo emailInfo) {
        def builder = Mock(EmailTemplateBuilder)
        emailTemplatesAPI.buildFromTemplate(_) >> builder
        builder.addAttachmentFile(_) >> builder
        builder.generateEmail() >> emailInfo
    }

    private void reportGenerationSucceedsWith(Report report, FileDescriptor storedReportFile) {
        reportingApi.createAndSaveReport(report, _, _, _) >> storedReportFile
    }

    private void noExtensionIsActive(ScheduledReport scheduledReport) {
        scheduledReportExtensionFactory.create(scheduledReport) >> new DefaultScheduledReportParameterExtension()
    }

    private void scheduledReportIsFound(ScheduledReport scheduledReport) {
        scheduledReportRepository.loadByCode('my_report', _) >> scheduledReport
    }
}
