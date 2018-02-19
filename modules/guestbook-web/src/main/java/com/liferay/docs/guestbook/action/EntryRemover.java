package com.liferay.docs.guestbook.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.liferay.docs.guestbook.model.Entry;
import com.liferay.docs.guestbook.service.EntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.ParamUtil;;

public class EntryRemover {

	private final ActionRequest request;
	private final ActionResponse response;
	private final EntryLocalService _entryLocalService;

	public EntryRemover(EntryLocalService _entryLocalService, ActionRequest request, ActionResponse response) {
		this._entryLocalService = _entryLocalService;
		this.request = request;
		this.response = response;
	}

	public void delete() throws PortalException {
		long entryId = ParamUtil.getLong(request, "entryId");
		long guestbookId = ParamUtil.getLong(request, "guestbookId");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(Entry.class.getName(), request);

		response.setRenderParameter("guestbookId", Long.toString(guestbookId));

		_entryLocalService.deleteEntry(entryId, serviceContext);
	}

}
