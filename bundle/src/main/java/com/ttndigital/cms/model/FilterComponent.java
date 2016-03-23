package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import java.util.*;

public class FilterComponent extends WCMUse {

    public Map<String, List> tagmap = new HashMap<String, List>();

    @Override
    public void activate() throws Exception {
        populatePageTags();
    }

    private void populatePageTags() {
        Iterator iterator;
        TagManager tagManager = getResourceResolver().adaptTo(TagManager.class);
        Tag tags[] = tagManager.getTags(getResource());
        for (Tag tag : tags) {
            iterator = tag.listAllSubTags();
            List<String> list = new ArrayList<>();
            while (iterator.hasNext()) {
                Tag childtag = (Tag) iterator.next();
                list.add(childtag.getTitle());
            }
            tagmap.put(tag.getTitle(), list);
        }
    }
}
