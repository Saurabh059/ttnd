package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.search.result.Hit;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.ttndigital.cms.SearchService;
import com.ttndigital.cms.TTNDConstants;
import com.ttndigital.cms.utility.TTNUtil;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Shikhar on 12/8/15.
 *
 *    List of hits
 * {
 *       title: blog_title
 *       url: blog_url
 *       modifiedBy: author
 *       modified: date_of_creation
 *       tags:list<tags>
 * }
 *
 * */
public class SearchListingComponent extends WCMUse{
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private SearchService searchService;
    private List<JSONObject> resultList = new ArrayList<JSONObject>();
    private SlingHttpServletRequest request;
    private ResourceResolver resolver;


    @Override
    public void activate() throws Exception {
        searchService = getSlingScriptHelper().getService(SearchService.class);
        request = getRequest();
        resolver = getResourceResolver();
        Integer offset = 0;
        Integer limit = 20;

        if (request != null && searchService != null) {
            Session session = (Session) resolver.adaptTo(Session.class);
            List<Hit> hits = searchService.searchContent(request.getParameter("s"), offset, limit, session, TTNDConstants.BLOG_SEARCH_PATH);
            if (!hits.isEmpty()) {
                for (Hit hit : hits) {
                    Resource hitResource = hit.getResource();
                    if (hitResource != null) {
                        PageManager pageManager = getPageManager();
                        Page hitPage = pageManager.getContainingPage(hitResource);
                        if (hitPage != null) {
                            TagManager tagManager = resolver.adaptTo(TagManager.class);
                            Tag[] tagArray = tagManager.getTags(hitPage.getContentResource());
                            List<String> tagList = new ArrayList<String>();
                            if (tagArray != null) {
                                tagList = TTNUtil.getTagsAsStringList(tagArray);
                            }
                            JSONObject resourceMap = new JSONObject();
                            resourceMap.put("title", hit.getTitle());
                            resourceMap.put("url", hit.getPath());
                            resourceMap.put("modifiedBy", hitPage.getLastModifiedBy());
                            resourceMap.put("modified", TTNUtil.convertDateToString(hitPage.getLastModified(), "EEE, d MMM yyyy"));
                            resourceMap.put("tags", tagList);

                            resultList.add(resourceMap);
                        }
                    }
                }
            } else {
                logger.info("result list is null");
            }
        }
    }

    public List<JSONObject> getResultList() {
        return resultList;
    }
}
