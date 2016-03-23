package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.wcm.api.Page;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This component class is used to store and process multifield values.
 */
public class TopNavComponent extends WCMUse {
    private Page page;
    private Logger log = Logger.getLogger(TopNavComponent.class.getName());
    private static int colRenderedCount = 1;


    public void activate() throws Exception {
        page = getPageManager().getPage((String) getCurrentStyle().get("rootPage"));
    }

    public Page getPage() {
        return page;
    }

    public Page getChild() {
        return get("child", Page.class);
    }

    public int getColumn() {
        if (get("headerPath", String.class) == null)
            return 1;
        else {
            Integer noOfcolumns = headerPathToColumnsMap().get(get("headerPath", String.class));
            return noOfcolumns == null ? 1 : noOfcolumns;
        }
    }

    /**
     * HashMap is populated with 'headerPath' as a key and columns as values.
     */
    public HashMap<String, Integer> headerPathToColumnsMap() {
        HashMap<String, Integer> headerPathToColumns = new HashMap<String, Integer>();
        try {

            String[] headerColArray = (String[]) getCurrentStyle().get("headerColumnArray", new String[0]);
            for (int i = 0; i < headerColArray.length; i++) {
                JSONObject jsonObject = new JSONObject(headerColArray[i]);
                headerPathToColumns.put(jsonObject.getString("headerPath"), jsonObject.getInt("columns"));
            }
        } catch (JSONException e) {
            log.info("Unable to parse Json.");
        }
        return headerPathToColumns;
    }

    public void initNoOfColRendered() {
        colRenderedCount = 1;
    }

    /**
     * If breakline returns true then <br/> tag is added.
     */
    public boolean breakLine() {
        long columnRendered = get("columnRendered", Long.class);
        boolean isBreak = ++columnRendered == getColumn() || columnRendered == colRenderedCount * getColumn();
        if (isBreak)
            colRenderedCount++;
        return isBreak;
    }
}


