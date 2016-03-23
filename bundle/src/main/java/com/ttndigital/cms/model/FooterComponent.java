package com.ttndigital.cms.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;

/**
 * The Class FooterComponent.
 */
public class FooterComponent extends WCMUse {
    
    /** The logger. */
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * The Path of the Root Page, taken by default in case of automatic
     * selection.
     */
    private final static String DEFAULT_ROOT_PATH = "/content/ttnd/en/homepage";
    
    /**
     * Gets a List of Map containing Parent Pages' Information including its
     * children.
     *
     * @return {@link List}
     */
    public List<Map<String, Object>> getFooterListofMap() {
	final PageFilter pageFilter = new PageFilter();
	final ResourceResolver resourceResolver = getResourceResolver();
	final Resource styleResource = resourceResolver.getResource(getCurrentStyle().getPath());
	final List<Map<String, Object>> listOfPages = new ArrayList<Map<String, Object>>();
	Resource resource;
	if (styleResource == null) {
	    resource = getDefaultResource(resourceResolver);
	} else {
	    final ValueMap valueMap = styleResource.adaptTo(ValueMap.class);
	    final String listFrom = valueMap.get("listFrom", String.class);
	    
	    if ("manual".equals(listFrom)) {
		addCustomPageList(resourceResolver, listOfPages, pageFilter);
	    } else { // for "automatic"
		final String parentPath = valueMap.get("parentPath", String.class);
		resource = resourceResolver.getResource(parentPath);
		if (resource == null) {
		    resource = getDefaultResource(resourceResolver);
		}
		
		final Page rootPage = resource.adaptTo(Page.class);
		final Iterator<Page> pages = rootPage.listChildren(pageFilter);
		while (pages.hasNext()) {
		    final Page page = pages.next();
		    addPageDetails(page.listChildren(pageFilter), page.getTitle(), listOfPages);
		}
	    }
	}
	return listOfPages;
    }
    
    /**
     * Adds the page details.
     *
     * @param subChildsItr the {@link Iterator} of the List of Sub-Children
     * @param title {@link String} the Title of the Parent to be displayed
     * @param listOfPages the {@link List} of All pages and their info
     */
    private void addPageDetails(final Iterator<Page> subChildsItr, final String title,
	    final List<Map<String, Object>> listOfPages) {
	final Map<String, Object> parentMap = new HashMap<String, Object>();
	parentMap.put("Iterator", subChildsItr);
	parentMap.put("Page", title);
	listOfPages.add(parentMap);
    }
    
    /**
     * Gets the default resource.
     *
     * @param resourceResolver {@link ResourceResolver}
     * @return the default resource
     */
    private Resource getDefaultResource(final ResourceResolver resourceResolver) {
	return resourceResolver.getResource(DEFAULT_ROOT_PATH);
    }
    
    /**
     * Fetches JSON from Repository containing custom list information and
     * updates a list of Map of required Pages' Info(Title and Child/Leaf Pages
     * of given 'rootpath').
     *
     * @param resourceResolver {@link ResourceResolver}
     * @param listOfPages {@link List} the list of pages
     * @param pageFilter {@link PageFilter} the page filter
     */
    private void addCustomPageList(final ResourceResolver resourceResolver,
	    final List<Map<String, Object>> listOfPages, final PageFilter pageFilter) {
	final String[] multiField = getCurrentStyle().get("footerArray", String[].class);
	if (multiField == null)
	    return;
	try {
	    for (final String field : multiField) {
		final JSONObject json = new JSONObject(field);
		final String title = (String) json.get("title");
		final String rootPath = (String) json.get("rootpath");
		final String childType = (String) json.get("childtype");
		final Resource resource = resourceResolver.getResource(rootPath);
		if (resource != null) {
		    final Page rootPage = resource.adaptTo(Page.class);
		    if (rootPage != null) {
			if ("children".equals(childType)) {
			    addPageDetails(rootPage.listChildren(pageFilter), title, listOfPages);
			} else if ("leaf".equals(childType)) {
			    addPageDetails(getLeafPages(rootPage, pageFilter).iterator(), title, listOfPages);
			}
		    }
		}
	    }
	} catch (final JSONException e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * Gets the leaf pages of given Page.
     *
     * @param rootPage {@link Page} the root page
     * @param pageFilter {@link PageFilter} the page filter
     * @return the {@link List} of leaf pages
     */
    private List<Page> getLeafPages(final Page rootPage, final PageFilter pageFilter) {
	final List<Page> leafPages = new ArrayList<Page>();
	final Iterator<Page> allChildren = rootPage.listChildren(pageFilter, true);
	while (allChildren.hasNext()) {
	    final Page child = allChildren.next();
	    if (!(child.listChildren(pageFilter).hasNext())) {
		leafPages.add(child);
	    }
	}
	return leafPages;
    }
    
    @Override
    public void activate() throws Exception {
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	logger.info("FooterComponent Implementation");
    }
}

