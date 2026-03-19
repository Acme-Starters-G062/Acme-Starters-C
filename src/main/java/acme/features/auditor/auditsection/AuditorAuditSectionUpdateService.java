
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
public class AuditorAuditSectionUpdateService extends AbstractService<Auditor, AuditSection> {

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

		boolean condition1 = this.auditSection.getAuditReport() != null;
		boolean condition2 = this.auditSection.getAuditReport().getDraftMode();
		boolean condition3 = this.auditSection.getAuditReport().getAuditor().isPrincipal();

		boolean status = condition1 && condition2 && condition3;

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.auditSection, "name", "notes", "hours", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.auditSection);
	}

	@Override
	public void execute() {
		this.repository.save(this.auditSection);
	}

	@Override
	public void unbind() {
		SelectChoices choices = SelectChoices.from(SectionKind.class, this.auditSection.getKind());

		Tuple tuple = super.unbindObject(this.auditSection, "name", "notes", "hours", "kind");
		tuple.put("draftMode", this.auditSection.getAuditReport().getDraftMode());
		tuple.put("kinds", choices);
		tuple.put("AuditReportId", super.getRequest().getData("AuditReportId", int.class));
	}

}
