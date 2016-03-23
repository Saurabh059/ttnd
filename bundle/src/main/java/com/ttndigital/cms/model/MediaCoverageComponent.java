package com.ttndigital.cms.model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import com.adobe.cq.sightly.WCMUse;

public class MediaCoverageComponent extends WCMUse {
	private String localeValue;
	private String formattedDate;
	
	public String getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}
	
	public String getLocaleValue() {
		return localeValue;
	}

	public void setLocaleValue(String localeValue) {
		this.localeValue = localeValue;
	}

	@Override
	public void activate() throws Exception {
		Locale locale = Locale.getDefault();
		setLocaleValue(locale.getLanguage());

		String mediaDate = (String) getProperties().get("mediaDate");
		Date date = new SimpleDateFormat("MM/dd/yy").parse(mediaDate);
		LocalDate dates = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		setFormattedDate((DateTimeFormatter.ofPattern("MMMM dd, uuuu").format(dates).toString()));
	}
}
