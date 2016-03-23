<%@page import="com.day.cq.wcm.foundation.forms.FieldHelper,
                com.day.cq.wcm.foundation.forms.FieldDescription,
                com.day.cq.wcm.foundation.forms.FormsHelper,
                org.apache.sling.scripting.jsp.util.JspSlingHttpServletResponseWrapper" %>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %>
<%
%><sling:defineObjects/><%
    final String regexp = "/^([\\w-\\.]+@(?!gmail.com)(?!yahoo.com)(?!hotmail.com)([\\w-]+\\.)+[\\w-]{2,4})?$/";
    final FieldDescription desc = FieldHelper.getConstraintFieldDescription(slingRequest);
    FieldHelper.writeClientRegexpText(slingRequest, new JspSlingHttpServletResponseWrapper(pageContext), desc, regexp);
%>
