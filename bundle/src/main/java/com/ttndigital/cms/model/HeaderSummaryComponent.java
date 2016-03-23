package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.ttndigital.cms.utility.TTNUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Shikhar on 5/8/15.
 */
public class HeaderSummaryComponent extends WCMUse{
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String modifiedDate;
    private String modifiedBy;

    private List<String> tags= new ArrayList<String>();
    private List<Tag> tagList=new ArrayList<Tag>();

    @Override
    public void activate() throws Exception {

        String pageTitle = getCurrentPage().getTitle();

        logger.info("ArticleComponent Implementation");

        Resource resource = getResource();
        ResourceResolver resourceResolver = getResourceResolver();

        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page containingPage = pageManager.getContainingPage(resource);

        modifiedDate = TTNUtil.convertDateToString(containingPage.getLastModified(),"EEE, d MMM yyyy");
        modifiedBy = containingPage.getLastModifiedBy();

        resource = containingPage.getContentResource();
        logger.info("HeaderSummaryComponent Implementation"+ resource.getPath());

        Tag[] tagArray = tagManager.getTags(resource);
        if(tagArray!=null)
            tags = TTNUtil.getTagsAsStringList(tagArray);
    }


    public List<String> getTags() {
        return tags;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }
}
