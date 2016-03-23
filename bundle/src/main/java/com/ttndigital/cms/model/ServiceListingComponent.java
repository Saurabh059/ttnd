package com.ttndigital.cms.model;


import com.adobe.cq.sightly.WCMUse;
import com.day.cq.wcm.api.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceListingComponent extends WCMUse {

    public static String servicePath;
    public Page page;
    private Logger log = LoggerFactory.getLogger(ServiceListingComponent.class);

    @Override
    public void activate() {
        listChildPages();
    }

    public void listChildPages() {
        page = getPageManager().getPage(getProperties().get("servicePath", String.class));
        log.info(" servicePath ------------->" + servicePath);
    }
}
