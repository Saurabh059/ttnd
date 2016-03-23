package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.wcm.api.Page;

import com.day.cq.wcm.api.designer.Style;

import org.json.JSONObject;
import org.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Shikhar on 30/7/15.
 */
public class BreadCrumbComponent extends WCMUse{

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String,String> breadcrumbs = new LinkedHashMap<String, String>();

    public Map<String, String> getBreadcrumbs() {
        return breadcrumbs;
    }

    @Override
    public void activate() throws Exception {

        /* We want to extract absParent and relParent properties from design_dialog's resource
         and then we want to build a list of breadcrumbs containing the path till relParent
        */

        Page currentPage = getCurrentPage();
        Style currentStyle = getCurrentStyle();
        String levelPath = currentStyle.get("absParent", String.class);
        Long relParent = currentStyle.get("relParent", Long.class);

        Page rootPage = currentPage.getAbsoluteParent(1);
        logger.info("title of root page ::::::::::::::: "+rootPage.getTitle());

        rootPage = levelPath!=null && !levelPath.isEmpty() ? getPageManager().getPage(levelPath):rootPage;
        long level = rootPage!=null ? rootPage.getDepth() - 1 : currentPage.getAbsoluteParent(1).getDepth();
        long endLevel = relParent != null ? relParent : 0l;

        int currentLevel = currentPage.getDepth();

        while (level < currentLevel - endLevel) {

            Page trail = currentPage.getAbsoluteParent((int) level);

            if (level != currentLevel)
                breadcrumbs.put(trail.getTitle(), trail.getPath());
            level++;
        }

    }
}
