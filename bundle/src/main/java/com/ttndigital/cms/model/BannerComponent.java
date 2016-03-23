package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This component class is used to populate a map with multifield values.
 */
public class BannerComponent extends WCMUse {
    private Logger log = Logger.getLogger(BannerComponent.class.getName());
    private HashMap<String, String> sectionContentMap = new HashMap<String, String>();

    @Override
    public void activate() throws Exception {
        try {
            String[] sectionContent = (String[]) getProperties().get("sectionContent", new String[0]);
            for (int i = 0; i < sectionContent.length; i++) {
                JSONObject jsonObject = new JSONObject(sectionContent[i]);
                sectionContentMap.put(jsonObject.getString("sectionTitle"), jsonObject.getString("sectionDescription"));
            }
        }catch (JSONException e) {
            log.info("Unable to parse Json.");
        }
    }

    public HashMap<String, String> getSectionContentMap() {
        return sectionContentMap;
    }
}
