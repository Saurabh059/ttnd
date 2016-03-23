package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.tagging.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This component class is used to populate a List containing Map of Parent
 * Information and their respective children.
 */
public class BlogComponent extends WCMUse {
    private static final String CONS_STRING_CATEGORY_TAG = "Category";
    private static final String CONS_STRING_CATEGORY_KEY = "category";
    private static final String CONS_STRING_TAGS_KEY = "tags";
    private static final Logger LOGGER = LoggerFactory.getLogger(MapComponent.class);
    
    @Override
    public void activate() throws Exception {
	LOGGER.info("Blog Component Activated");
    }

    /**
     * Fetches Tags for the Current Page and categories them into Tags and
     * Categories as per their hierarchy.
     *
     * @return {@link Map}
     */
    public Map<String, List<Tag>> getCategorizedTags() {
	final List<Tag> categoryTags = new ArrayList<Tag>();
	final List<Tag> otherTags = new ArrayList<Tag>();
	final Map<String, List<Tag>> tagsMap = new HashMap<String, List<Tag>>();
	for (final Tag tag : getResourcePage().getTags()) {
	    final Tag parentTag = tag.getParent();
	    if (parentTag != null && CONS_STRING_CATEGORY_TAG.equals(parentTag.getTitle())) {
		categoryTags.add(tag);
	    } else {
		otherTags.add(tag);
	    }
	}
	tagsMap.put(CONS_STRING_CATEGORY_KEY, categoryTags);
	tagsMap.put(CONS_STRING_TAGS_KEY, otherTags);
	return tagsMap;
    }
}
