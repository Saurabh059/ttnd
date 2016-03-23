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
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Shikhar on 27/9/15.
 */

public class RelatedCaseStudiesComponent extends WCMUse{
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private SearchService searchService;
    private ResourceResolver resolver;

    private List<JSONObject> resultList = new ArrayList<JSONObject>();
    private String tagTitle;

    @Override
    public void activate() throws Exception {

        String tagCriteria = getProperties().get("tagName", String.class);
        Integer limit = getProperties().get("limit",Integer.class);
        Tag searchTag = null;

        searchService = getSlingScriptHelper().getService(SearchService.class);
        resolver = getResourceResolver();
        PageManager pageManager = getPageManager();

        Session session = (Session) resolver.adaptTo(Session.class);
        TagManager tagManager = resolver.adaptTo(TagManager.class);

        Page containingPage = pageManager.getContainingPage(getResource());
        Tag[] tags = tagManager.getTags(containingPage.getContentResource());
        List<Tag> tagList = Arrays.asList(tags);

        for(Tag tag : tagList){
            if(tag.getTagID().indexOf(tagCriteria.toLowerCase()) != -1){
                tagTitle = tag.getTitle();
                searchTag = tag;
                break;
            }
        }
        if(searchTag!=null && searchService!=null){
            List<Hit> pages = searchService.searchTaggedContent(searchTag, TTNDConstants.DEFAULT_OFFSET, limit, session, TTNDConstants.CASE_STUDIES_SEARCH_PATH);
            if(!pages.isEmpty()){
                for(Hit page : pages) {
                    try {
                        Resource pageResource = page.getResource();
                        Page containingPageOfResource = pageManager.getContainingPage(pageResource);
                        JSONObject caseStudiesObject = TTNUtil.getCaseStudiesData(containingPageOfResource);
                        resultList.add(caseStudiesObject);
                    } catch (RepositoryException exception) {
                        logger.error("Exception occurred ",exception);

                    }
                }
            }

        }
    }

    public List<JSONObject> getResultList() {
        return resultList;
    }

    public String getTagTitle() {
        return tagTitle;
    }
}
