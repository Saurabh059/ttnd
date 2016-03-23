package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.tagging.Tag;
import com.day.cq.wcm.api.Page;
import com.ttndigital.cms.TTNDConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This component class is used to populate a map with multifield values.
 */
public class JobDetailComponent extends WCMUse {
    private Logger log = LoggerFactory.getLogger(JobDetailComponent.class);
    private Map<String, String> jobLocations = new HashMap<String, String>();

    @Override
    public void activate() throws Exception {
        Page page = getPageManager().getContainingPage(getResource());
        Tag[] tags = page.getTags();
        List<String> locationArray = new ArrayList<String>();
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].getParent().getName().equals(TTNDConstants.LOCATION))
                locationArray.add(tags[i].getTitle());
        }
        String locations = locationArray.toString();
        jobLocations.put("Job Location", locations.substring(1, locations.length() - 1));
    }

    public Map<String, String> getJobLocations() {
        return jobLocations;
    }

}
