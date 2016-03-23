package com.ttndigital.cms.impl;


import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;
import com.day.cq.wcm.api.PageManager;
import com.ttndigital.cms.utility.TTNUtil;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.PropertyUnbounded;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import javax.servlet.ServletException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

@Component(metatype = true, label = "TTND-Site Map Servlet", description = "Site Map Servlet",
        configurationFactory = true)
@Service
@Properties({@Property(name = "sling.servlet.resourceTypes", unbounded = PropertyUnbounded.ARRAY,
        label = "Sling Resource Type", description = "Sling Resource Type for the Home Page component or components."),
        @Property(name = "sling.servlet.extensions", value = "xml", propertyPrivate = true),
        @Property(name = "sling.servlet.methods", value = "GET", propertyPrivate = true)})
public class SiteMapServlet extends SlingSafeMethodsServlet {
    private static final String PRIORITY = "priority";
    private static final String CHANGE_FREQ = "changefreq";
    private static final String DEFAULT_PRIORITY = "0.5";
    private static final String DEFAULT_CHANGE_FREQ = "Monthly";
    private static final String SITEMAP_URL = "http://www.sitemaps.org/schemas/sitemap/0.9";

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType(request.getResponseContentType());
        PageManager pageManager = (PageManager) request.getResourceResolver().adaptTo(PageManager.class);
        Page page = pageManager.getContainingPage(request.getResource());
        XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
        try {
            XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(response.getWriter());
            streamWriter.writeStartDocument("1.0");
            streamWriter.writeStartElement("", "urlset", SITEMAP_URL);
            streamWriter.writeNamespace("", SITEMAP_URL);
            InheritanceValueMap valueMap = new HierarchyNodeInheritanceValueMap(page.getContentResource());
            Page parentPage = page.getParent();
            Iterator children = parentPage.listChildren(new PageFilter(), true);
            String domainName = valueMap.getInherited("domainName", String.class);
            if (domainName != null) {
                while (children.hasNext()) {
                    this.write((Page) children.next(), streamWriter, domainName);
                }
            }
            streamWriter.writeEndElement();
            streamWriter.writeEndDocument();
        } catch (XMLStreamException streamException) {
            throw new IOException(streamException);
        }
    }

    private void write(Page page, XMLStreamWriter stream, String domainName) throws XMLStreamException {
        stream.writeStartElement(SITEMAP_URL, "url");
        stream.writeStartElement(SITEMAP_URL, "loc");
        String loc = domainName + String.format("%s.html", new Object[]{page.getPath()});
        stream.writeCharacters(loc);
        stream.writeEndElement();
        stream.writeStartElement(SITEMAP_URL, "lastmod");
        Calendar lastModified = page.getLastModified();
        stream.writeCharacters(TTNUtil.convertDateToString(lastModified, "yyyy-MM-dd hh:mm:ss"));
        stream.writeEndElement();
        stream.writeStartElement(SITEMAP_URL, CHANGE_FREQ);
        String changefreq = page.getProperties().get(CHANGE_FREQ, String.class);
        if (changefreq == null)
            changefreq = DEFAULT_CHANGE_FREQ;
        stream.writeCharacters(changefreq);
        stream.writeEndElement();
        stream.writeStartElement(SITEMAP_URL, PRIORITY);
        String priority = page.getProperties().get(PRIORITY, String.class);
        if (priority == null)
            priority = DEFAULT_PRIORITY;
        stream.writeCharacters(priority);
        stream.writeEndElement();
        stream.writeEndElement();
    }

}
