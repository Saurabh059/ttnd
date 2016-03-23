<%@page session="false" %>
<%@include file="/libs/foundation/global.jsp" %>
<%@ page import="com.day.cq.wcm.foundation.TextFormat,
                 com.day.cq.wcm.foundation.forms.FormsHelper,
                 com.day.cq.wcm.foundation.forms.LayoutHelper,
                 com.day.cq.wcm.foundation.forms.FormResourceEdit,
                 java.util.ResourceBundle,
                 com.day.cq.i18n.I18n" %>
<%

    final ResourceBundle resourceBundle = slingRequest.getResourceBundle(null);
    I18n i18n = new I18n(resourceBundle);

    final String name = FormsHelper.getParameterName(resource);
    final String id = FormsHelper.getFieldId(slingRequest, resource);
    final boolean required = FormsHelper.isRequired(resource);
    final boolean readOnly = FormsHelper.isReadOnly(slingRequest, resource);
    final boolean hideTitle = properties.get("hideTitle", false);
    final String placeholder = properties.get("placeholder", String.class);
    final String width = properties.get("width", String.class);
    final int rows = xssAPI.getValidInteger(properties.get("rows", String.class), 1);
    final int cols = xssAPI.getValidInteger(properties.get("cols", String.class), 35);
    String[] values = FormsHelper.getValues(slingRequest, resource);
    if (values == null) {
        values = new String[]{""};
    }

    String title = i18n.getVar(FormsHelper.getTitle(resource, "Text"));


    String mrName = name + FormResourceEdit.WRITE_SUFFIX;
%>
<div class="form_row">
    <% LayoutHelper.printTitle(id, title, required, hideTitle, out); %>
    <div class="form_rightcol" id="<%= xssAPI.encodeForHTMLAttr(name) %>_rightcol"><%
        int i = 0;
        for (String value : values) {
    %>
        <div id="<%= xssAPI.encodeForHTMLAttr(name) %>_<%= i %>_wrapper" class="form_rightcol_wrapper"><%
            if (readOnly) {
                if (value.length() == 0) {
                    // at least display a space otherwise layout may break
                    value = " ";
                } // CQ-26294 - show hide requires a field to bind to so we need a hidden input
        %><input type="hidden" disabled name="<%= xssAPI.encodeForHTMLAttr(name)%>"><%
        %><%= new TextFormat().format(value) %><%
        } else {
            String currentId = i == 0 ? id : id + "-" + i;
            if (rows == 1) {
        %><input class="<%= FormsHelper.getCss(properties, "form_field form_field_text") %>" <%
            %>id="<%= xssAPI.encodeForHTMLAttr(currentId) %>" <%
                     %>name="<%= xssAPI.encodeForHTMLAttr(name) %>" <%
                     %>value="<%= xssAPI.encodeForHTMLAttr(value) %>" <%  if(placeholder!=null) {
                            %>placeholder="<%= xssAPI.encodeForHTMLAttr(placeholder) %>" <% }
                            %>size="<%= cols %>" <%
                            if (width != null) {
                                %>style="width:<%= xssAPI.getValidInteger(width, 100) %>px;" <%
                            }
                        %> ><% if (required) {%><span class="star"></span><% }
        } else {
        %><textarea class="<%= FormsHelper.getCss(properties, "form_field form_field_textarea") %>" <%
            %>id="<%= xssAPI.encodeForHTMLAttr(currentId) %>" <%
                        %>name="<%= xssAPI.encodeForHTMLAttr(name) %>" <% if(placeholder!=null) {
                            %>placeholder="<%= xssAPI.encodeForHTMLAttr(placeholder) %>" <% }
                            %>rows="<%= rows %>" cols="<%= cols %>" <%
                            if (width != null) {
                                %>style="width:<%= xssAPI.getValidInteger(width, 100) %>px;" <%
            }
        %> ><%= xssAPI.encodeForHTML(value) %></textarea><% if (required) { %><span class="star"></span><%
                    }
                }
            }
            i++;
        %></div>
        <%
            }
        %></div>
</div>
<%

    LayoutHelper.printDescription(FormsHelper.getDescription(resource, ""), out);
    boolean errorPrinted = false;
    for (int j = 0; j < values.length; j++) {
        // constraints (e.g. "number") are checked per field (multiple fields when multi value)
        errorPrinted = LayoutHelper.printErrors(slingRequest, name, out, j);
        if (errorPrinted) break;
    }
    if (!errorPrinted) {
        // check mandatory and single values constraints
        LayoutHelper.printErrors(slingRequest, name, out);
    }
%>
