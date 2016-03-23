package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import org.apache.sling.api.resource.Resource;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This component class is used to populate all child resources of CaseStudies component.
 */
public class CaseStudyComponent extends WCMUse {
    private ArrayList<Resource> caseStudiesList;

    @Override
    public void activate() throws Exception {
        Resource caseStudyResource = getResource().getChild("case_study");
        caseStudiesList = new ArrayList<Resource>();
        Iterator<Resource> children = caseStudyResource.listChildren();
        while (children.hasNext() && caseStudiesList.size()!=3) {
            Resource caseStudy = children.next();
            caseStudiesList.add(caseStudy);
        }
    }

    public ArrayList<Resource> getCaseStudiesList(){
        return caseStudiesList;
    }
}

