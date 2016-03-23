package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.tagging.Tag;
import com.ttndigital.cms.TTNDConstants;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to populate map with page tags
 */
public class CaseStudyShelf extends WCMUse {
    private Map<String, String> pageTags;

    @Override
    public void activate() throws Exception {
        populatePageTags();
    }

    public Map<String, String> getPageTags() {
        return pageTags;
    }

    private void populatePageTags() {
        pageTags = new HashMap<String, String>();
        for (Tag tag : getPageManager().getContainingPage(getResource()).getTags()) {
            Tag parentTag = tag.getParent();
            String parentTagName = parentTag.getName();
            if (parentTagName.equals(TTNDConstants.SERVICE_TAG) || parentTagName.equals(TTNDConstants.INDUSTRY_TAG) || parentTagName.equals(TTNDConstants.COUNTRY_TAG) || parentTagName.equals(TTNDConstants.RESOURCE_TAG) )
                pageTags.put(parentTag.getTitle(), tag.getTitle());
        }
    }
}
