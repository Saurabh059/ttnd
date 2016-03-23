package com.ttndigital.cms.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.Tag;
import com.ttndigital.cms.SearchService;
import com.ttndigital.cms.TTNDConstants;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Shikhar on 11/8/15.
 * Service exposed to jcr for providing search capabilities to the user
 */

@Component(metatype = false)
@Service(value = SearchService.class)
public class SearchServiceImpl implements SearchService{
    private static final long serialVersionUID = 2598426539166789515L;

    final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Reference
    private SlingRepository repository;

    @Reference
    private QueryBuilder builder;


    private List<Hit> searchHelper(Map<String,String> searchMap, Session session, Integer offset, Integer limit){

        List<Hit> resultList = Collections.emptyList();

        //Create a Query instance
        Query query = builder.createQuery(PredicateGroup.create(searchMap), session);

        if(null != offset)
            query.setStart(offset);

        if(null != limit)
            query.setHitsPerPage(limit);

        LOGGER.info("query is {} ::::::::::::: ",query.toString());
        //getting reference of query result into SearchResult object
        SearchResult result = query.getResult();
        if(null != result)
            resultList = result.getHits();

        return resultList;

    }

    public List<Hit> searchContent(String fulltextSearchTerm, Integer offset, Integer limit,Session session, String path ) {
        LOGGER.info("full text term :::: " + fulltextSearchTerm);

        fulltextSearchTerm = fulltextSearchTerm!=null ? fulltextSearchTerm : "";
        Map<String, String> searchMap = new HashMap<String, String>();

        searchMap.put(TTNDConstants.TYPE, "cq:Page");

        searchMap.put(TTNDConstants.PATH, path);
        // create query description as hash map (simplest way)
        searchMap.put("group.1_fulltext", fulltextSearchTerm);
        searchMap.put("group.2_fulltext", fulltextSearchTerm);

        // can be done in map or with Query methods
        searchMap.put("p.offset", offset.toString()); // same as query.setStart(0) below
        searchMap.put("p.limit", limit.toString()); // same as query.setHitsPerPage(20) below

        searchMap.put("group.p.or", "true"); // combine this group with OR
        searchMap.put("group.1_fulltext.relPath", "jcr:content");
        searchMap.put("group.2_fulltext.relPath", "jcr:content/@cq:tags");
        searchMap.put(TTNDConstants.ORDER_BY, "@created");
        searchMap.put(TTNDConstants.ORDER_BY_SORT, "desc");


        return searchHelper(searchMap, session, offset, limit);

    }

    @Override
    public List<Hit> searchTaggedContent(Tag searchTag, Integer offset, Integer limit, Session session, String path) {

        Map<String, String> searchMap = new HashMap<String, String>();

        searchMap.put(TTNDConstants.PATH, path);
        searchMap.put(TTNDConstants.TYPE, "cq:Page");
        searchMap.put("tagid", searchTag.getTagID());
        searchMap.put("tagid.property","jcr:content/cq:tags");

        return searchHelper(searchMap, session, offset, limit);
    }

    @Override
    public List<Hit> searchPagesInPathLastModifiedDesc(Session session, String path, Integer offset, Integer limit) {

        Map<String, String> pageDetailMap = new HashMap<String, String>();

        pageDetailMap.put(TTNDConstants.PATH, path);
        pageDetailMap.put(TTNDConstants.TYPE, "cq:Page");
        pageDetailMap.put(TTNDConstants.ORDER_BY, "@jcr:content/cq:lastModified");
        pageDetailMap.put(TTNDConstants.ORDER_BY_SORT, "desc");

        return searchHelper(pageDetailMap,session,offset,limit);
    }


}
