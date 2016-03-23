package com.ttndigital.cms.utility;

import com.adobe.acs.commons.email.EmailService;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.ComponentContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This servlet is used to send emails.
 */
@SlingServlet(paths = {"/bin/ttnd/emailServices"},generateComponent = false)
@Component(metatype = true)

public class EmailServiceProvider extends SlingAllMethodsServlet {
    private Logger log = Logger.getLogger(EmailServiceProvider.class.getName());
    @Reference
    private EmailService emailService;

    @Property(value="/etc/notification/email/html/emailTemplate.txt", label = "Template Path")
    private static final String TEMPLATE_PATH = "template.path";
    private static String templatePath;

    @Activate
    protected void activate(ComponentContext context)
    {
        templatePath = (String)context.getProperties().get(TEMPLATE_PATH);
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        Map<String, String> emailParams = new HashMap<String,String>();
        emailParams.put("senderName", "AMC Team");
        List<String> failureList = null;
        try {
            failureList = emailService.sendEmail(templatePath, emailParams, request.getParameter("emailId"));
            if (failureList.isEmpty()) {
                log.info("Email sent successfully to the recipients");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
                log.info("Email sent failed");
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
            log.info("IllegalArgumentException occured");
        }
    }

}
