package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by ttnd on 22/9/15.
 */
public class ColumnControlComponent extends WCMUse {
    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private Integer columns = null;
    private Map<String,String> columnedClasses = new LinkedHashMap<String,String>();

    @Override
    public void activate() throws Exception {
        ValueMap properties = getProperties();
        columns = properties.get("columnlayout") != null ? properties.get("columnlayout", Integer.class) : 1;
        Boolean sectionClass = properties.get("sideBorder")!=null ? properties.get("sideBorder", Boolean.class) : false;
        Integer partitionsWidth = columns!=null ? (12/columns) : 12;
        for(int i=0;i<columns;i++) {

            StringBuffer staticClasses = new StringBuffer("col-xs-12 col-sm-6");

            if(!sectionClass)
                staticClasses.append(" section");

            columnedClasses.put("parsys" + i,
                    staticClasses.append(" col-md-" + partitionsWidth).toString());

        }
    }

    public Integer getColumns() {
        return columns;
    }

    public Map<String,String> getColumnedClasses() {
        return columnedClasses;
    }

}
