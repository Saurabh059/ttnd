package com.ttndigital.mcm.form.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ttndigital.cms.EmailServer;

@Component(immediate = true, metatype = false, label = "Newsletter Subscription")
@Service(value = { NewsLetterSubscription.class })
public class NewsLetterSubscription {
    
    /**
     * Name of the Request Parameter for Groups to which user is to be
     * subscribed to.
     */
    private static final String CONS_REQUEST_PARAM_GROUP_NAMES = "subscription-group-names";
    
    private static final String CONS_REQUEST_PARAM_EMAIL = "subscriber-email";
    private static final String CONS_PROPERTY_AUTHORIZABLE_CATEGORY = "cq:authorizableCategory";
    private static final String CONS_PROPERTY_VALUE_MCM = "mcm";
    private static final String CONS_PROPERTY_USER_EMAIL = "profile/email";
    private static final String emailBody = "You Have been Subscribed to ";
    private static final Logger logger = LoggerFactory.getLogger(NewsLetterSubscription.class);
    
    @Reference
    private EmailServer emailServer;

    /**
     * Creates a user with given email(param name : "subscriber-email"), if not
     * already present, and subscribes it to all the groups defined in the
     * request parameters(param name : "subscription-group-names").
     *
     * @param request {@link SlingHttpServletRequest}
     * @param response {@link SlingHttpServletResponse}
     */
    public void subscribeToGroups(final SlingHttpServletRequest request, final SlingHttpServletResponse response) {
	final String[] groupNames = request.getParameterValues(CONS_REQUEST_PARAM_GROUP_NAMES);
	final String email_id = request.getParameter(CONS_REQUEST_PARAM_EMAIL);
	if (groupNames != null) {
	    try {
		final ResourceResolver resourceResolver = request.getResourceResolver();
		final Session session = resourceResolver.adaptTo(Session.class);
		final ValueFactory valueFactory = session.getValueFactory();
		final UserManager userManager = resourceResolver.adaptTo(UserManager.class);
		if (userManager != null) {
		    final Authorizable user = userManager.getAuthorizable(email_id);
		    final List<String> subscribedGroups = new ArrayList<String>();
		    User subscriberUser;
		    if (user == null) {
			subscriberUser = userManager.createUser(email_id, RandomStringUtils.randomAlphanumeric(20)
				.toUpperCase());
			subscriberUser.setProperty(CONS_PROPERTY_AUTHORIZABLE_CATEGORY,
				valueFactory.createValue(CONS_PROPERTY_VALUE_MCM));
			subscriberUser.setProperty(CONS_PROPERTY_USER_EMAIL, valueFactory.createValue(email_id));
		    } else {
			subscriberUser = (User) userManager.getAuthorizable(email_id);
		    }
		    for (final String groupName : groupNames) {
			final Group group = (Group) userManager.getAuthorizable(groupName);
			if (group != null) {
			    group.addMember(subscriberUser);
			    subscribedGroups.add(groupName);
			}
		    }
		    session.save();
		    sendEmail(email_id, emailBody + subscribedGroups);
		}
	    } catch (final RepositoryException e) {
		logger.error(e.getMessage());
	    }
	}
    }

    /**
     * Populates email param map with given email id and sends an email.
     *
     * @param emailId {@link String}
     * @param message {@link String}
     */
    private void sendEmail(final String emailId, final String message) {
	final Map<String, String> emailParams = new HashMap<String, String>();
	emailParams.put("name", emailId);
	emailParams.put("message", message);
	emailParams.put("emailId", emailId);
	emailServer.sendMail(emailParams);

    }
    
}
