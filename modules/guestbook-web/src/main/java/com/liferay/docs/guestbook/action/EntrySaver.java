package com.liferay.docs.guestbook.action;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import com.liferay.docs.guestbook.model.Entry;
import com.liferay.docs.guestbook.service.EntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;;

public class EntrySaver {
	
	
	
	private final ActionRequest request; 
	private final ActionResponse response;
	private final EntryLocalService _entryLocalService;
	
	public EntrySaver(EntryLocalService _entryLocalService, ActionRequest request, ActionResponse response){
		this._entryLocalService = _entryLocalService;
		this.request = request;
		this.response = response;
	}
	
	
	public void save() throws ReadOnlyException, ValidatorException, IOException, PortalException {
		ServiceContext serviceContext = ServiceContextFactory.getInstance(
	            Entry.class.getName(), request);

	        String userName = ParamUtil.getString(request, "name");
	        String email = ParamUtil.getString(request, "email");
	        String message = ParamUtil.getString(request, "message");
	        long guestbookId = ParamUtil.getLong(request, "guestbookId");
	        long entryId = ParamUtil.getLong(request, "entryId");

	    if (entryId > 0) {

	        try {

	            _entryLocalService.updateEntry(
	                serviceContext.getUserId(), guestbookId, entryId, userName,
	                email, message, serviceContext);

	            SessionMessages.add(request, "entryAdded");

	            response.setRenderParameter(
	                "guestbookId", Long.toString(guestbookId));

	        }
	        catch (Exception e) {
	            System.out.println(e);

	            SessionErrors.add(request, e.getClass().getName());

	            PortalUtil.copyRequestParameters(request, response);

	            response.setRenderParameter(
	                "mvcPath", "/guestbookwebportlet/add_entry.jsp");
	        }

	    }
	    else {

	        try {
	            _entryLocalService.addEntry(
	                serviceContext.getUserId(), guestbookId, userName, email,
	                message, serviceContext);

	            SessionMessages.add(request, "entryAdded");

	            response.setRenderParameter(
	                "guestbookId", Long.toString(guestbookId));

	        }
	        catch (Exception e) {
	            SessionErrors.add(request, e.getClass().getName());

	            PortalUtil.copyRequestParameters(request, response);

	            response.setRenderParameter(
	                "mvcPath", "/guestbookwebportlet/edit_entry.jsp");
	        }
	    }
		
		
	}


}
