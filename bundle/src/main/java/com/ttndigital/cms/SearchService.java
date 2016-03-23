package com.ttndigital.cms;

import com.day.cq.search.result.Hit;
import com.day.cq.tagging.Tag;

import javax.jcr.Session;
import java.util.List;

/**
 * Created by Shikhar on 21/8/15.
 */
public interface SearchService {
    public List<Hit> searchContent(String fulltextSearchTerm, Integer offset, Integer limit, Session session, String path);
    public List<Hit> searchTaggedContent(Tag tag,Integer offset, Integer limit,Session session, String path);
    public List<Hit> searchPagesInPathLastModifiedDesc(Session session, String path,Integer offset, Integer limit);
}
