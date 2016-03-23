package com.ttndigital.cms.impl;

import com.day.cq.commons.RangeIterator;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.ttndigital.cms.TTNDConstants;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This servlet is used to provide json with job details.
 */
@SlingServlet(paths = {"/services/ttnd/jobDetail"}, generateComponent = false)
@Component(metatype = true)
public class JobDetailServlet extends SlingAllMethodsServlet {
    private Logger log = LoggerFactory.getLogger(JobDetailServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        int id = 1;
        JSONObject jobsData = new JSONObject();
        List<JSONObject> jobsList = new ArrayList<JSONObject>();
        ResourceResolver resourceResolver = request.getResourceResolver();
        PageManager pageMgr = resourceResolver.adaptTo(PageManager.class);
        TagManager tagMgr = resourceResolver.adaptTo(TagManager.class);
        RangeIterator<Resource> functionTagResources = tagMgr.find(TTNDConstants.CAREER_FUNCTIONS_TAGS);
        log.info("functionTagResources ::::::"+functionTagResources.getSize());
        while (functionTagResources.hasNext()) {
            Page page = pageMgr.getContainingPage(functionTagResources.next());
            JSONObject jobDetailMap = getJobDetails(page, id);
            jobsList.add(jobDetailMap);
            id++;
        }
        try {
            jobsData.put("data", jobsList);
        } catch (JSONException e) {
            log.error("Unable to parse object.");
        }
        response.getWriter().write(jobsData.toString());
    }

    /**
     * used to populate a json object with job details.
     */
    private JSONObject getJobDetails(Page page, int id) {
        JSONObject jobDetailMap = new JSONObject();
        List<String> locationArray = new ArrayList<String>();
        String experience = page.getProperties().get(TTNDConstants.EXPERIENCE, String.class);
        Tag[] tags = page.getTags();
        try {
            jobDetailMap.put("id", id);
            for (int i = 0; i < tags.length; i++) {
                if (tags[i].getParent().getName().equals(TTNDConstants.LOCATION))
                    locationArray.add(tags[i].getTitle());
                else if (tags[i].getParent().getName().equals(TTNDConstants.FUNCTIONS))
                    jobDetailMap.put("function", tags[i].getTitle());
                jobDetailMap.put("location", locationArray);
            }
            jobDetailMap.put("url", page.getPath() + ".html");
            jobDetailMap.put("job", page.getTitle());
            jobDetailMap.put("experience", (experience != null ? experience : "---"));

        } catch (JSONException e) {
            log.error("Unable to parse object.");
        }
        return jobDetailMap;
    }
}
