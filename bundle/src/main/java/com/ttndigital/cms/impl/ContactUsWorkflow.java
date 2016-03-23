package com.ttndigital.cms.impl;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import com.ttndigital.cms.EmailServer;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Workflow process for Contactus components
 */
@Service
@Component(metatype = true, immediate = true)
@Property(name = "process.label", value = "ContactUs Workflow")
public class ContactUsWorkflow implements WorkflowProcess {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Reference
    private EmailServer emailServer;
    private Map<String, String> emailParams;

    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        ResourceResolver resourceResolver = getResourceResolver(workflowSession.getSession());
        Resource resource = resourceResolver.getResource(workItem.getWorkflowData().getPayload().toString());
        ValueMap valueMap = resource.adaptTo(ValueMap.class);
        initEmailParams(valueMap);
        emailServer.sendMail(emailParams);
    }

    /*
        populating emailparam map with the property-values of a resource node.
     */
    private void initEmailParams(ValueMap valueMap) {
        emailParams = new HashMap<String, String>();
        emailParams.put("name", valueMap.get("fullname", String.class));
        emailParams.put("message", valueMap.get("message", String.class));
        emailParams.put("emailId", valueMap.get("emailId", String.class));
        emailParams.put("resourceName", valueMap.get("resourceName", String.class));
        emailParams.put("resourcePath", valueMap.get("resourcePath", String.class));

    }

    private ResourceResolver getResourceResolver(Session session) {
        try {
            return resourceResolverFactory.getResourceResolver(Collections.<String, Object>singletonMap(JcrResourceConstants.AUTHENTICATION_INFO_SESSION,
                    session));
        } catch (org.apache.sling.api.resource.LoginException e) {
            log.error("LoginException occurred.");
        }
        return null;
    }
}
