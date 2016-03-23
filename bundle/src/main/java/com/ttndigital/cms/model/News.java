package com.ttndigital.cms.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;

public class News implements Comparable<News>{
	private final Logger LOGGER = LoggerFactory.getLogger(NewsFeedComponent.class);
	private Page page;
	private String newsDate;

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public String getNewsDate() {
		return newsDate;
	}

	public void setNewsDate(String newsDate) {
		this.newsDate = newsDate;
	}
	
	@Override
	public int compareTo(News news) {
		DateFormat dateFormat = DateFormat.getDateInstance();
		Date newsDate1 = null, newsDate2 = null;
		
		try {
			newsDate1 = dateFormat.parse(getNewsDate());
			newsDate2 = dateFormat.parse(news.getNewsDate());
		} catch (ParseException e) {
			LOGGER.error("Parse Exception");
		}

		if (newsDate1 == null || newsDate2 == null)
			return 0;
		
		return newsDate2.compareTo(newsDate1);
	}
}
