package com.ttndigital.cms.model;

import static com.google.common.base.Preconditions.checkNotNull;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.search.result.Hit;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.ttndigital.cms.SearchService;
import com.ttndigital.cms.TTNDConstants;
import org.apache.felix.scr.annotations.Component;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Session;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
*  Component class used to populate all news content.
*/

@Component(metatype = true)
public class NewsFeedComponent extends WCMUse {
    private final Logger LOGGER = LoggerFactory.getLogger(NewsFeedComponent.class);
    private ArrayList<News> newsList = new ArrayList<News>();
    private SearchService searchService;
    
	@Override
    public void activate() throws Exception {
		LOGGER.info("NewFeed Component Implementation");
		
        searchService = getSlingScriptHelper().getService(SearchService.class);
		ResourceResolver resourceResolver = getResourceResolver();
		Session session = resourceResolver.adaptTo(Session.class);
		
		String[] newsUrl = getProperties().get("url",String[].class);
		int newsCount = (null != newsUrl) ? newsUrl.length : 0;

		//If the author gives the implicitly specifies the URL  
		if (newsCount >= 1) {
			for (int i = 0; i < newsCount; i++) {
				JSONObject newsJson = new JSONObject(newsUrl[i]);
				News news = new News();
				PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
				Page page = pageManager.getPage(newsJson.getString("newsUrl"));
				if (null != page) {
					news.setPage(page);
					news.setNewsDate(getNewsDate(page));
					newsList.add(news);
				}
			}
		} else {
			List<Hit> result = searchService.searchPagesInPathLastModifiedDesc(
					session, TTNDConstants.NEWS_PATH,TTNDConstants.DEFAULT_OFFSET, null);

			for (Hit hit : result) {
				News news = new News();
				Page page = getPageManager().getContainingPage(hit.getResource());
				if(null != page) {
					news.setPage(page);
					news.setNewsDate(getNewsDate(page));
					newsList.add(news);
				}
			}
		}
		Collections.sort(newsList);
    }

    public int getPageLimit() {
        return getProperties().get("limit", 0);
    }

    public ArrayList<News> getNewsList() {
		return newsList;
	}
    
	public String getNewsDate(Page page) {
		final String DATE_PATH = "news-details/newsDate";
		Resource resource = checkNotNull(page).getContentResource();
		String formattedDate = null;
		Date date = null;

		if (null != resource) {
			ValueMap valueMap = resource.adaptTo(ValueMap.class);
			date = valueMap.get(DATE_PATH, Date.class);			
		}
		
		if(null != date) {
			LocalDate dates = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			formattedDate = DateTimeFormatter.ofPattern("MMMM dd, uuuu").format(dates).toString();			
		}
		return formattedDate;
	}    
}
