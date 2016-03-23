package com.ttndigital.cms.model;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SitemapComponent extends WCMUse {

    private static final Logger LOGGER = LoggerFactory.getLogger(SitemapComponent.class);
    private Integer depth;
    private PrintWriter out;
    private String rootPath;
    private List<SiteLink> links = new LinkedList<SiteLink>();

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void activate() throws Exception {
        populateLinks(getRequest().getResourceResolver().adaptTo(PageManager.class).getPage(getRootPath()), 0);
        setOut(getResponse().getWriter());
        drawPrintList();
    }

    public String getRootPath() {
        rootPath = getProperties().get("rootPath", String.class);
        if (StringUtils.isEmpty(rootPath)) {
            Long absParent = getCurrentStyle().get("absParent", 2L);
            rootPath = getCurrentPage().getAbsoluteParent(absParent.intValue()).getPath();
        }
        return rootPath;
    }

    public void drawPrintList() {
        try {
            printList(getOut());
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    private void populateLinks(Page page, Integer level) {

        setDepth(getProperties().get("depth", Integer.class));
        if (page != null) {
            String title = page.getTitle();
            if (title == null) title = page.getName();
            links.add(new SiteLink(page.getPath(), title, level));
            Iterator<Page> children = page.listChildren(new PageFilter());
            while (children.hasNext()) {
                Page child = children.next();
                if (getDepth() != null) {
                    if (level < getDepth())
                        populateLinks(child, level + 1);
                    else
                        break;
                } else
                    populateLinks(child, level + 1);
            }
        }
    }

    public void printList(PrintWriter out) throws IOException {
        int previousLevel = -1;
        for (SiteLink aLink : links) {
            if (aLink.getLevel() > previousLevel) out.print("<div class=\"linkcontainer\">");
            else if (aLink.getLevel() < previousLevel) {
                for (int i = aLink.getLevel(); i < previousLevel; i++)
                    out.print("</div>");
            }
            out.printf("<div class=\"link\"><a href=\"%s.html\">%s</a></div>", StringEscapeUtils.escapeHtml4(aLink.getPath()), StringEscapeUtils.escapeHtml4(aLink.getTitle()));
            previousLevel = aLink.getLevel();
        }
        for (int i = -1; i < previousLevel; i++)
            out.print("</div>");
    }

    class SiteLink {
        private String path;
        private Integer level;
        private String title;

        public SiteLink(String path, String title, Integer level) {
            this.path = path;
            this.level = level;
            this.title = title;
        }

        public String getPath() {
            return path;
        }

        public Integer getLevel() {
            return level;
        }

        public String getTitle() {
            return title;
        }
    }
}
