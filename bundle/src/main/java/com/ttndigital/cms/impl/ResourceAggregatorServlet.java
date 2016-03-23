package com.ttndigital.cms.impl;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.ttndigital.cms.TTNDConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

@SlingServlet(paths = {"/services/ttnd/aggregator"}, generateComponent = false)
@Component(metatype = true)
public class ResourceAggregatorServlet extends SlingAllMethodsServlet {
    private Logger log = LoggerFactory.getLogger(ResourceAggregatorServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String resourcePagePath = request.getParameter("path");
        PrintWriter printWriter = response.getWriter();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObjectData = new JSONObject();
        response.setContentType("application/json");
        if (!StringUtils.isEmpty(resourcePagePath)) {
            Iterator<Resource> child = request.getResourceResolver().getResource(resourcePagePath).listChildren();
            try {
                int id = 1;
                while (child.hasNext()) {
                    JSONObject jsonObject = new JSONObject();
                    Page page = child.next().adaptTo(Page.class);
                    jsonObject.put("title", page.getProperties().get(JcrConstants.JCR_TITLE, String.class));
                    jsonObject.put("id", id);
                    Tag[] tags = page.getTags();
                    for (int i = 0; i < tags.length; i++) {
                        if (tags[i].getParent().getName().equals(TTNDConstants.RESOURCE_TAG)) {

                            jsonObject.put("resource", tags[i].getName());
                            if (tags[i].getName().equals("webinar") || tags[i].getName().equals("screencast")) {
                                jsonObject.put("downloadText", "View " + tags[i].getName());
                                jsonObject.put("downloadImg", "/etc/designs/ttnd/images/view-icon.png");


                            } else {
                                jsonObject.put("downloadText", "Download " + tags[i].getName());
                                jsonObject.put("downloadImg", "/etc/designs/ttnd/images/download-icon.png");
                            }
                        }
                        if (tags[i].getParent().getName().equals(TTNDConstants.SERVICE_TAG)) {
                            jsonObject.put("function", tags[i].getName());
                        }
                    }
                    Resource resource = request.getResourceResolver().getResource(page.getPath() + TTNDConstants.IMAGE_REFERENCE);
                    ValueMap map = resource.adaptTo(ValueMap.class);
                    jsonObject.put("imageSrc", map.get("fileReference", String.class));
                    jsonObject.put("url",page.getPath() + ".html");
                    jsonArray.put(jsonObject);
                    id++;
                }
                jsonObjectData.put("data", jsonArray);

            } catch (JSONException e) {
                log.error(e.getMessage());
            }
            printWriter.println(jsonObjectData);
        }
    }
}
