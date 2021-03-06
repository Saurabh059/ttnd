<%@include file="/libs/foundation/global.jsp" %>
<%@ page import="com.day.cq.i18n.I18n,
                 com.day.cq.wcm.api.WCMMode,
                 com.day.cq.wcm.foundation.ELEvaluator,
                 com.day.cq.wcm.foundation.forms.FormsHelper,
                 com.day.cq.wcm.foundation.forms.LayoutHelper" %>
<%
    I18n i18n = new I18n(slingRequest);

    final String name = FormsHelper.getParameterName(resource);
    final String id = FormsHelper.getFieldId(slingRequest, resource);

    if (WCMMode.fromRequest(request) == WCMMode.EDIT) {
%>
<div class="form_row cq-form-hidden-placeholder">
    <% LayoutHelper.printTitle(id, null, false, out); %>
    <div class="form_rightcol"><%= i18n.get("Hidden Field") %>
    </div>
</div>
<%
    }
    String value = FormsHelper.getValue(slingRequest, resource);
    if (value == null) {
        value = "";
    }
    // run though ELEvaluator to resolve variables
    value = ELEvaluator.evaluate(value, slingRequest, pageContext);
%><input id="<%=xssAPI.encodeForHTMLAttr(id)%>" name="<%=xssAPI.encodeForHTMLAttr(name)%>"
         value="<%=xssAPI.encodeForHTMLAttr(value)%>" type="hidden"><%
%>
