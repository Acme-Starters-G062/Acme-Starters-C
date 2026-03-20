
package acme.features.auditor.auditsection;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.auditsection.AuditSection;
import acme.realms.auditor.Auditor;

@Controller
public class AuditorAuditSectionController extends AbstractController<Auditor, AuditSection> {

	// Constructors -----------------------------------------------------------

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		super.addBasicCommand("list", AuditorAuditSectionListService.class);
		super.addBasicCommand("show", AuditorAuditSectionShowService.class);
		super.addBasicCommand("create", AuditorAuditSectionCreateService.class);
		super.addBasicCommand("update", AuditorAuditSectionUpdateService.class);
		super.addBasicCommand("delete", AuditorAuditSectionDeleteService.class);
	}
}
