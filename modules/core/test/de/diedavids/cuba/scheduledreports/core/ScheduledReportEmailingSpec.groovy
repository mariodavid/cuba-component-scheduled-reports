package de.diedavids.cuba.scheduledreports.core

import com.haulmont.addon.emailtemplates.builder.EmailTemplateBuilder
import com.haulmont.addon.emailtemplates.core.EmailTemplatesAPI
import com.haulmont.addon.emailtemplates.entity.JsonEmailTemplate
import com.haulmont.cuba.core.app.EmailerAPI
import com.haulmont.cuba.core.entity.FileDescriptor
import com.haulmont.cuba.core.entity.ScheduledExecution
import com.haulmont.cuba.core.global.DataManager
import com.haulmont.cuba.core.global.EmailInfo
import com.haulmont.cuba.core.global.TimeSource
import com.haulmont.reports.entity.Report
import com.haulmont.reports.entity.ReportTemplate
import de.diedavids.cuba.scheduledreports.entity.ScheduledReport
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution
import spock.lang.Specification

class ScheduledReportEmailingSpec extends Specification {
    DataManager dataManager
    EmailerAPI emailerAPI
    EmailTemplatesAPI emailTemplatesAPI
    ScheduledReportEmailing sut
    FileDescriptor reportFile
    ScheduledReportExecution scheduledReportExecution


    def setup() {
        dataManager = Mock(DataManager)

        emailerAPI = Mock(EmailerAPI)
        emailTemplatesAPI = Mock(EmailTemplatesAPI)
        sut = new ScheduledReportEmailing(
                dataManager: dataManager,
                emailTemplatesAPI: emailTemplatesAPI,
                emailerAPI: emailerAPI
        )


        reportFile = new FileDescriptor()
        scheduledReportExecution = new ScheduledReportExecution(
                reportFile: reportFile
        )
    }


    def "sendEmailForScheduledReport attaches the generated report file as an attachment"() {

        given:
        def emailTemplate = new JsonEmailTemplate(
                to: "some@email.com",
                subject: "your report..."
        )

        and:
        def builder = Mock(EmailTemplateBuilder)
        emailTemplatesAPI.buildFromTemplate(_) >> builder

        when:
        sut.sendEmailForScheduledReport(emailTemplate, this.scheduledReportExecution)

        then:
        1 * builder.addAttachmentFile(reportFile) >> builder
    }

    def "sendEmailForScheduledReport triggers Emailer API to send out the email asynchronously"() {

        given:
        def emailTemplate = new JsonEmailTemplate(
                to: "some@email.com",
                subject: "your report..."
        )
        and:
        def emailInfo = Mock(EmailInfo)
        emailTemplatesCreatesTheEmail(emailInfo)

        when:
        sut.sendEmailForScheduledReport(emailTemplate, scheduledReportExecution)

        then:
        1 * emailerAPI.sendEmailAsync(emailInfo)

    }

    private void emailTemplatesCreatesTheEmail(EmailInfo emailInfo) {
        def builder = Mock(EmailTemplateBuilder)
        emailTemplatesAPI.buildFromTemplate(_) >> builder
        builder.addAttachmentFile(_) >> builder
        builder.generateEmail() >> emailInfo
    }

}
