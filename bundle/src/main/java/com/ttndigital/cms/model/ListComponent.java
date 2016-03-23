package com.ttndigital.cms.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import com.adobe.cq.sightly.WCMUse;

/**
 * This component class is used to populate a List containing Map of Parent
 * Information and their respective children.
 */
public class ListComponent extends WCMUse {
    private Resource listComponent;
    private static final String CONS_PROP_TITLE = "title";
    private static final String CONS_PROP_PATH = "path";
    private static final String CONS_PROP_CHILDREN = "children";

    @Override
    public void activate() throws Exception {
	final ResourceResolver resourceResolver = getResourceResolver();
	listComponent = resourceResolver.getResource(getCurrentStyle().getPath());
    }
    
    /**
     * Returns the List Containing Parent Information including their children.
     *
     * @return {@link List}
     */
    public List<Map<String, Object>> getListOfParents() {
	final List<Map<String, Object>> listOfParents = new ArrayList<Map<String, Object>>();
	if (listComponent != null) {
	    final Iterator<Resource> parentIterator = listComponent.listChildren();
	    while (parentIterator.hasNext()) {
		final Map<String, Object> parentInfo = new HashMap<String, Object>();
		final Resource parent = parentIterator.next();
		final Iterator<Resource> childrenIterator = parent.listChildren();
		final ValueMap parentProperties = parent.adaptTo(ValueMap.class);
		parentInfo.put(CONS_PROP_TITLE, parentProperties.get(CONS_PROP_TITLE));
		parentInfo.put(CONS_PROP_PATH, parentProperties.get(CONS_PROP_PATH));
		parentInfo.put(CONS_PROP_CHILDREN, childrenIterator);
		listOfParents.add(parentInfo);
	    }
	}
	return listOfParents;
    }
}
