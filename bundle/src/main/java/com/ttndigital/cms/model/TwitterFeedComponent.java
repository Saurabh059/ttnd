package com.ttndigital.cms.model;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.osgi.service.component.ComponentContext;

/**
 * This Component class is used for configuring properties of Twitter account.
 */
@Component(metatype = true)
public class TwitterFeedComponent {

    @Property(label = "Tweeter Username")
    private static final String TWITTER_USERNAME = "twitter.username";
    private static String twitterUserName;

    @Property(label = "Widget Id")
    private static final String WIDGET_ID = "widget.id";
    private static String widgetId;

    @Activate
    protected void activate(ComponentContext context)
    {
        twitterUserName = (String)context.getProperties().get(TWITTER_USERNAME);
        widgetId = (String)context.getProperties().get(WIDGET_ID);
    }

    public String getTwitterUserName() {
        return twitterUserName;
    }

    public String getWidgetId() {
        return widgetId;
    }
}
