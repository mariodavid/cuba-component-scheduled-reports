package de.diedavids.cuba.scheduledreports.core;

import com.haulmont.addon.emailtemplates.core.EmailTemplatesAPI;
import com.haulmont.addon.emailtemplates.entity.EmailTemplate;
import com.haulmont.addon.emailtemplates.exceptions.ReportParameterTypeChangedException;
import com.haulmont.addon.emailtemplates.exceptions.TemplateNotFoundException;
import com.haulmont.cuba.core.app.EmailerAPI;
import com.haulmont.cuba.core.entity.SendingMessage;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EmailInfo;
import de.diedavids.cuba.scheduledreports.entity.ScheduledReportExecution;
import de.diedavids.cuba.scheduledreports.service.ScheduledReportRunServiceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component(ScheduledReportEmailing.NAME)
public class ScheduledReportEmailing {
    public static final String NAME = "ddcsr_ScheduledReportEmailing";


    private static final Logger log = LoggerFactory.getLogger(ScheduledReportRunServiceBean.class);


    @Inject
    protected EmailTemplatesAPI emailTemplatesAPI;
    @Inject
    protected EmailerAPI emailerAPI;

    @Inject
    protected DataManager dataManager;

    public void sendEmailForScheduledReport(EmailTemplate emailTemplate, ScheduledReportExecution scheduledReportExecution){

        log.info("Email for scheduled report is send");
        EmailTemplate reloadedEmailTemplate = dataManager.reload(emailTemplate, "emailTemplate-view");
        try {

            List<SendingMessage> sendingMessages = triggerEmailSending(scheduledReportExecution, reloadedEmailTemplate);
            scheduledReportExecution.setSendingMessages(sendingMessages);

            dataManager.commit(scheduledReportExecution);


        } catch (TemplateNotFoundException e) {
            log.error("Email Template not found for scheduled report", e);
        } catch (ReportParameterTypeChangedException e) {
            log.error("Email Template could not be rendered because of wrong parameter types", e);
        }
    }

    private List<SendingMessage> triggerEmailSending(ScheduledReportExecution scheduledReportExecution, EmailTemplate emailTemplate) throws ReportParameterTypeChangedException, TemplateNotFoundException {
        EmailInfo emailInfo = emailTemplatesAPI.buildFromTemplate(emailTemplate)
                .addAttachmentFile(
                        scheduledReportExecution
                                .getReportFile()
                )
                .generateEmail();

        return emailerAPI.sendEmailAsync(emailInfo);
    }
}