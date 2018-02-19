package com.liferay.docs.guestbook.portlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ValidatorException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.docs.guestbook.action.EntryRemover;
import com.liferay.docs.guestbook.action.EntrySaver;
import com.liferay.docs.guestbook.constants.GuestbookPortletKeys;
import com.liferay.docs.guestbook.model.Guestbook;
import com.liferay.docs.guestbook.service.EntryLocalService;
import com.liferay.docs.guestbook.service.GuestbookLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.ParamUtil;

/**
 * @author julio-souza
 */
@Component(immediate = true, property = { 
		"com.liferay.portlet.display-category=category.social",
		"com.liferay.portlet.instanceable=false", 
		"com.liferay.portlet.scopeable=true",
		"javax.portlet.display-name=Guestbook", 
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.name=" + GuestbookPortletKeys.GUESTBOOK,
		"javax.portlet.init-param.view-template=/guestbookwebportlet/view.jsp",
		"javax.portlet.resource-bundle=content.Language", 
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.supports.mime-type=text/html" 
		}, 
service = Portlet.class)
public class GuestbookPortlet extends MVCPortlet {

	public static final String GUESTBOOK_ENTRIES = "guestbook-entries";

	public void addEntry(ActionRequest request, ActionResponse response) {
		try {
			new EntrySaver( _entryLocalService,request, response).save();
		} catch (ReadOnlyException | IOException | ValidatorException | PortalException ex) {
			Logger.getLogger(GuestbookPortlet.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void deleteEntry(ActionRequest request, ActionResponse response)  {
		try {
			new EntryRemover(_entryLocalService, request, response).delete();
		} catch (PortalException e) {
			Logger.getLogger(GuestbookPortlet.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws PortletException, IOException {

		try {
            ServiceContext serviceContext = ServiceContextFactory.getInstance(
                Guestbook.class.getName(), renderRequest);

            long groupId = serviceContext.getScopeGroupId();

            long guestbookId = ParamUtil.getLong(renderRequest, "guestbookId");

            List<Guestbook> guestbooks = _guestbookLocalService.getGuestbooks(
                groupId);

            if (guestbooks.isEmpty()) {
                Guestbook guestbook = _guestbookLocalService.addGuestbook(
                    serviceContext.getUserId(), "Main", serviceContext);

                guestbookId = guestbook.getGuestbookId();
            }

            if (guestbookId == 0) {
                guestbookId = guestbooks.get(0).getGuestbookId();
            }

            renderRequest.setAttribute("guestbookId", guestbookId);
        }
        catch (Exception e) {
            throw new PortletException(e);
        }

        super.render(renderRequest, renderResponse);	
	}
	
	@Reference(unbind = "-")
    protected void setEntryService(EntryLocalService entryLocalService) {
        _entryLocalService = entryLocalService;
    }

    @Reference(unbind = "-")
    protected void setGuestbookService(GuestbookLocalService guestbookLocalService) {
        _guestbookLocalService = guestbookLocalService;
    }

    private EntryLocalService _entryLocalService;
    private GuestbookLocalService _guestbookLocalService;	

}