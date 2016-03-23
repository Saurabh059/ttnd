package com.ttndigital.cms.impl;

import com.adobe.acs.commons.email.EmailService;
import com.ttndigital.cms.EmailServer;
import org.apache.felix.scr.annotations.*;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Implementation class used for sending mails.
 */
@Service(value = EmailServer.class)
@Component(metatype = true, immediate = true)
public class EmailServerImpl implements EmailServer {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String USER_EMAIL_ID = "emailId";

    @Reference
    private EmailService emailService;

    @Property(value = "/etc/notification/email/html/formTemplate.txt", label = "Form Template Path")
    private static final String FORM_TEMPLATE_PATH = "formTemplate.path";
    private static String formTemplatePath;

    @Property(value = "/etc/notification/email/html/confirmTemplate.txt", label = "Comfirmation Template Path")
    private static final String CONFIRM_TEMPLATE_PATH = "confirmTemplate.path";
    private static String confirmTemplatePath;

    @Property(value = "/etc/notification/email/html/errorTemplate.txt", label = "Error Template Path")
    private static final String ERROR_TEMPLATE_PATH = "errorTemplate.path";
    private static String errorTemplatePath;

    @Property(value = "/etc/notification/email/html/downloadResourceTemplate.html", label = "Resource download Template Path")
    private static final String RESOURCE_DOWNLOAD_TEMPLATE_PATH = "resourceDownloadTemplate.path";
    private static String resourceDownloadTemplatePath;

    @Property(value = "Amc@tothenew.com", label = "Email Id")
    private static final String EMAIL_ID = "email.id";
    private static String emailId;

    @Activate
    protected void activate(ComponentContext context) {
        formTemplatePath = (String) context.getProperties().get(FORM_TEMPLATE_PATH);
        confirmTemplatePath = (String) context.getProperties().get(CONFIRM_TEMPLATE_PATH);
        errorTemplatePath = (String) context.getProperties().get(ERROR_TEMPLATE_PATH);
        emailId = (String) context.getProperties().get(EMAIL_ID);
        resourceDownloadTemplatePath = (String) context.getProperties().get(RESOURCE_DOWNLOAD_TEMPLATE_PATH);
    }


    public void sendMail(Map<String, String> emailParams) {
        List<String> failureList = null;
        try {
            failureList = emailService.sendEmail(formTemplatePath, emailParams, emailId);
            if (failureList.isEmpty()) {
                if (emailParams.get("resourcePath") != null) {

                    emailService.sendEmail(resourceDownloadTemplatePath, emailParams, emailParams.get(USER_EMAIL_ID));
                } else {

                    emailService.sendEmail(confirmTemplatePath, emailParams, emailParams.get(USER_EMAIL_ID));
                }
            } else {
                emailService.sendEmail(errorTemplatePath, emailParams, emailParams.get(USER_EMAIL_ID));
            }
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException occured");
        }
    }
}
