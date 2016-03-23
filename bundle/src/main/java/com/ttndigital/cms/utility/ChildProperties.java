package com.ttndigital.cms.utility;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.adobe.cq.sightly.WCMUse;

/**
 * The Class ChildProperties.
 */
public class ChildProperties extends WCMUse {
    
    private ValueMap childProperties;

    /*
     * Initializes the childProperties ValueMap with the properties of the child
     * of resource with given child name.
     */
    @Override
    public void activate() throws Exception {
	final String childNode = get("child", String.class);
	final Resource childResource = getResource().getChild(childNode);
	if (childResource != null) {
	    childProperties = childResource.adaptTo(ValueMap.class);
	}
    }

    /**
     * Returns the child properties in the form of ValueMap.
     *
     * @return {@link ValueMap}
     */
    public ValueMap getChildProperties() {
	return childProperties;
    }
}
