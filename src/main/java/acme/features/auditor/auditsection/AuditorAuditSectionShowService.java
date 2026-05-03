
package acme.features.auditor.auditsection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.auditsection.AuditSection;
import acme.entities.auditsection.SectionKind;
import acme.realms.auditor.Auditor;

@Service
public class AuditorAuditSectionShowService extends AbstractService<Auditor, AuditSection> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditSectionRepository	repository;

	private AuditSection					auditSection;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);

		this.auditSection = this.repository.findAuditSectionById(id);
	}

	@Override
	public void authorise() {
		int auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean isOwner = this.auditSection != null && this.auditSection.getAuditReport().getAuditor().getId() == auditorId;

		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Auditor.class) && isOwner;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		SelectChoices choices = SelectChoices.from(SectionKind.class, this.auditSection.getKind());

		Tuple tuple = super.unbindObject(this.auditSection, "name", "notes", "hours", "kind");
		tuple.put("draftMode", this.auditSection.getAuditReport().getDraftMode());
		tuple.put("kinds", choices);
		tuple.put("auditReportId", this.auditSection.getAuditReport().getId());
	}

}
