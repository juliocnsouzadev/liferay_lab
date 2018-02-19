<%@include file="../init.jsp"%>

<% //long guestbookId = Long.valueOf((Long) renderRequest.getAttribute("guestbookId"));
long guestbookId = 1l;//paleativo %>

<aui:button-row cssClass="guestbook-buttons">

    <portlet:renderURL var="addEntryURL">
        <portlet:param name="mvcPath" value="/guestbookwebportlet/edit_entry.jsp" />
        <portlet:param name="guestbookId" value="<%=String.valueOf(guestbookId)%>" />
    </portlet:renderURL>

    <aui:button onClick="<%=addEntryURL.toString()%>" value="Add Entry"></aui:button>

</aui:button-row>

<liferay-ui:search-container total="<%=EntryLocalServiceUtil.getEntriesCount()%>">
<liferay-ui:search-container-results
    results="<%=EntryLocalServiceUtil.getEntries(
    					//scopeGroupId.longValue(),
    					1l, //peleativo
                    	guestbookId, searchContainer.getStart(),
                    	searchContainer.getEnd()
                    )%>"
/>

<liferay-ui:search-container-row
    className="com.liferay.docs.guestbook.model.Entry" modelVar="entry">

    <liferay-ui:search-container-column-text property="message" />

    <liferay-ui:search-container-column-text property="name" />

</liferay-ui:search-container-row>

<liferay-ui:search-iterator />

</liferay-ui:search-container>