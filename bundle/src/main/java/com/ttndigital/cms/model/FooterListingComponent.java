package com.ttndigital.cms.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.cq.sightly.WCMUse;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;

public class FooterListingComponent extends WCMUse {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
	private List<Page> list = new ArrayList<Page>();
	private String rootTitle;

	@Override
	public void activate() {
		String parentPage = getProperties().get("parentPage", String.class);
		String title = getProperties().get("title", String.class);
		String listPages[] = getProperties().get("listPages",String[].class);
		int listPagesCount = (null != listPages) ? listPages.length : 0;
		
		ResourceResolver resourceResolver = getResourceResolver();
				
		if(null != parentPage && !validate(listPages,listPagesCount)) {
			logger.info("Automatic Listing");			
			Resource resource = resourceResolver.getResource(parentPage);
			Page rootPage = resource.adaptTo(Page.class);
			Iterator<Page> pagesIterator = rootPage.listChildren(new PageFilter());
			while (pagesIterator.hasNext()) {
				Page page = pagesIterator.next();
				list.add(page);
			}
			rootTitle = (title == "" || null == title) ? rootPage.getTitle() : title;
		}else if (listPagesCount > 0 && validate(listPages,listPagesCount)) {
			logger.info("Manual Listing");
			Page parent = null;		
			String parentTitle = null;
			
			for (int i = 0; i < listPagesCount; i++) {
				JSONObject newsJson;
				try {
					newsJson = new JSONObject(listPages[i]);
					PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
					Page page = pageManager.getPage(newsJson.getString("individualPages"));							
					if (null != page) {
						list.add(page);
						parent = page.getParent();	
					}
				} catch (JSONException e) {
					logger.error("JSONException");
				}
			}			
			if (null != parent) {
				parentTitle = parent.getTitle();
			}
			rootTitle = (title == "" || null == title) ? parentTitle : title;
		}
	}

	public String getRootTitle() {
		return rootTitle;
	}

	public List<Page> getList() {
		return list;
	}
	
	public boolean validate(String listPages[],int count) {
		int flag = 0;
		if(count > 0){
			for (int i = 0; i < count; i++) {
				JSONObject newsJson;
				try {
					newsJson = new JSONObject(listPages[i]);
					PageManager pageManager = getResourceResolver().adaptTo(PageManager.class);
					Page page = pageManager.getPage(newsJson.getString("individualPages"));											
					if (null != page) {
						flag ++;
						if(flag > 0) return true;
					}	
				} catch (JSONException e) {
					logger.error("JSONException");
				}			
			}			
		}
		return false;
	}
}
