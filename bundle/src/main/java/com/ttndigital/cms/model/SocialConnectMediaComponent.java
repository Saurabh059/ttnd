package com.ttndigital.cms.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *To fetch multiple values authored in Social Media Connect Component and return them after parsing for view.
 */
@Model(adaptables = Resource.class)
public class SocialConnectMediaComponent {

    @Inject
    @Named("socialmedia")
    private List<String> socialMedia;

    private List<SocialMediaObject> socialMediaObjectList;

    private Logger logger = LoggerFactory.getLogger(SocialConnectMediaComponent.class);

    @PostConstruct
    public List<SocialMediaObject> getSocialMediaObjectList() {

        socialMediaObjectList = new ArrayList<SocialMediaObject>();

        try {
            if (socialMedia!=null) {
                Iterator<String> sMedia = socialMedia.iterator();
                while (sMedia.hasNext()) {

                    SocialMediaObject smobj = new SocialMediaObject();
                    JSONParser parser = new JSONParser();

                    Object obj = parser.parse(sMedia.next().toString());
                    JSONObject jsonObject = (JSONObject) obj;

                    smobj.setName((String) jsonObject.get("socialName"));
                    smobj.setLink((String) jsonObject.get("socialLink"));
                    socialMediaObjectList.add(smobj);
                }

            }
        } catch (ParseException pe) {

            logger.error("JSON cannot be parsed");
        }

        return socialMediaObjectList;
    }
}
