package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.commons.jcr.JcrUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Class is used to populate Sticky Nav Titles
 */
public class StickyNav extends WCMUse {
    private String navTitleId;
    private Map navTitles;

    public String getNavTitleId() {
        return navTitleId;
    }

    private void setNavTitleId(String navTitleId) {
        this.navTitleId = navTitleId;
    }

    public Map<String, String> getNavTitles() {
        return navTitles;
    }

    @Override
    public void activate() throws Exception {
        String navId = get("navTitleId", String.class);
        if (navId != null)
            setNavTitleId(JcrUtil.createValidName(navId));
        populateNavTitles();
    }

    private void populateNavTitles() {
        navTitles = new LinkedHashMap<String, String>();
        Iterator<Resource> iterator = getCurrentPage().getContentResource().getChild("pagecontent").listChildren();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            ValueMap resourceProperties = resource.adaptTo(ValueMap.class);
            String navTitle = resourceProperties.get("navTitle", String.class);
            if (navTitle != null) {
                navTitles.put(navTitle, JcrUtil.createValidName(navTitle));
            }
        }
    }
}
