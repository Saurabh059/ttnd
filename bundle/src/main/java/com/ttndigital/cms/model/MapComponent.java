package com.ttndigital.cms.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUse;

/**
 * The Class MapComponent.
 */
public class MapComponent extends WCMUse {
    private static final String CONST_STRING_STATISTIC = "statistic";
    private static final String CONST_STRING_PROPERTY = "property";
    private static final Logger LOGGER = LoggerFactory.getLogger(MapComponent.class);
    String[] multiField;

    /*
     * Initializes the MultiField String Array as per the given name of the
     * MultiField Property.
     */
    @Override
    public void activate() throws Exception {
	final String multiFieldPropertyName = get("multiField", String.class);
	if (multiFieldPropertyName != null) {
	    multiField = getResource().adaptTo(ValueMap.class).get(multiFieldPropertyName, String[].class);
	}
    }

    /**
     * Returns the List of MultiFields after converting them to JSON Objects.
     *
     * @return {@link List}
     */
    public List<Map<String, Object>> getMultiFieldJson() {
	final List<Map<String, Object>> listOfMultiFieldJson = new ArrayList<Map<String, Object>>();
	if (multiField != null) {
	    try {
		for (final String field : multiField) {
		    final JSONObject json = new JSONObject(field);
		    addJsonToList(json, listOfMultiFieldJson);
		}
	    } catch (final JSONException e) {
		LOGGER.error(e.getMessage());
	    }
	}
	return listOfMultiFieldJson;
    }

    /**
     * Adds JSON Object, after converting it to Map, to the given List.
     *
     * @param json {@link JSONObject}
     * @param listOfMultiFieldJson {@link List}
     * @throws JSONException {@link JSONException}
     */
    private void addJsonToList(final JSONObject json, final List<Map<String, Object>> listOfMultiFieldJson)
	    throws JSONException {
	final Map<String, Object> multiField = new HashMap<String, Object>();
	multiField.put(CONST_STRING_STATISTIC, json.get(CONST_STRING_STATISTIC));
	multiField.put(CONST_STRING_PROPERTY, json.get(CONST_STRING_PROPERTY));
	listOfMultiFieldJson.add(multiField);
    }
}
