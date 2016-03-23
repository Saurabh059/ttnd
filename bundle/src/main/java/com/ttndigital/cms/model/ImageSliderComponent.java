package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.commons.json.JSONObject;

public class ImageSliderComponent extends WCMUse {
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	private ArrayList<Slide> sliderContentList = new ArrayList<Slide>();
	
	public ArrayList<Slide> getSliderContentList() {
		return sliderContentList;
	}

	@Override
	public void activate() throws Exception {
		logger.info("Image Slider Component Implementation");

		String[] sliderContent = getProperties().get("sliderContent",String[].class);
		int slides = sliderContent.length;

		for (int i = 0; i < slides; i++) {
			JSONObject sliderJson = new JSONObject(sliderContent[i]);
			Slide slide = new Slide();
			slide.setSliderImage(sliderJson.getString("sliderImage"));
			slide.setSliderHeader(sliderJson.getString("description"));
			slide.setSliderFooter(sliderJson.getString("footer"));
			slide.setSliderContentImage(sliderJson.getString("sliderContentImage"));
			sliderContentList.add(slide);
		}
	}
}
