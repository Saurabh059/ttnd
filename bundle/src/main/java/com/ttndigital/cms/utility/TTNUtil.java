package com.ttndigital.cms.utility;

import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Shikhar on 27/8/15.
 */
public class TTNUtil {

    /*
    * different formats
    * "yyyy.MM.dd G 'at' HH:mm:ss z"	2001.07.04 AD at 12:08:56 PDT
    * "EEE, MMM d, ''yy"	Wed, Jul 4, '01
    * "h:mm a"	12:08 PM
    * "hh 'o''clock' a, zzzz"	12 o'clock PM, Pacific Daylight Time
    * "K:mm a, z"	0:08 PM, PDT
    * "yyyyy.MMMMM.dd GGG hh:mm aaa"	02001.July.04 AD 12:08 PM
    * "EEE, d MMM yyyy HH:mm:ss Z"	Wed, 4 Jul 2001 12:08:56 -0700
    * "yyMMddHHmmssZ"	010704120856-0700
    * "yyyy-MM-dd'T'HH:mm:ss.SSSZ"	2001-07-04T12:08:56.235-0700
    *
    *
    * */


    public static String convertDateToString(Calendar calendarDate, String format){
        Date date = calendarDate.getTime();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(format);
        String modifiedDate = DATE_FORMAT.format(date);
        return modifiedDate;
    }

    public static String convertDateToString(Date date, String format){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(format);
        String modifiedDate = DATE_FORMAT.format(date);
        return modifiedDate;
    }

    public static List<String> getTagsAsStringList(Tag[] tagArray){
        List<Tag> tagList = Arrays.asList(tagArray);
        List<String> tags = new ArrayList<String>();
        for(Tag tag: tagList){
            tags.add(tag.getTitle());
        }
        return tags;
    }

    public static JSONObject getCaseStudiesData(Page page){

        // final strings inside this method to limit the scope of the paths being used
        final String HERO_IMAGE_PATH = "pagecontent/hero/desktop/fileReference";
        final String DESC_PATH = "pagecontent/hero/description";
        final String LOGO_IMAGE_PATH = "pagecontent/case_study_shelf/logoAndDesc/fileReference";

        JSONObject jsonObject = new JSONObject();

        Resource resource = checkNotNull(page).getContentResource();
        if(null != resource){
            ValueMap valueMap = resource.adaptTo(ValueMap.class);

            String heroImage = valueMap.get(HERO_IMAGE_PATH, String.class);
            String description = valueMap.get(DESC_PATH, String.class);
            String logoImage = valueMap.get(LOGO_IMAGE_PATH, String.class);

            jsonObject.put("title",checkNotNull(page).getTitle());
            jsonObject.put("path",checkNotNull(page).getPath());

            if(null != heroImage)
                jsonObject.put("heroImage", heroImage);

            if(null != description)
                jsonObject.put("description", Jsoup.parse(description).text());

            if(null != logoImage)
                jsonObject.put("logoImage",logoImage);

        }
        return jsonObject;
    }



}

