
package acme.features.auditor.auditsection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.auditreport.AuditReport;
import acme.entities.auditsection.AuditSection;
import acme.entities.auditsection.SectionKind;
import acme.realms.auditor.Auditor;

@Service
public class AuditorAuditSectionCreateService extends AbstractService<Auditor, AuditSection> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditSectionRepository	repository;

	private AuditSection					auditSection;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int auditReportId = super.getRequest().getData("auditReportId", int.class);
		AuditReport auditReport = this.repository.findAuditReportById(auditReportId);

		this.auditSection = super.newObject(AuditSection.class);
		this.auditSection.setAuditReport(auditReport);
	}

	@Override
	public void authorise() {
		int auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();

		AuditReport auditReport = this.auditSection == null ? null : this.auditSection.getAuditReport();

		boolean isDraft = auditReport != null && Boolean.TRUE.equals(auditReport.getDraftMode());
		boolean isOwner = auditReport != null && auditReport.getAuditor().getId() == auditorId;

		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Auditor.class) && isOwner && isDraft;

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
		tuple.put("auditReportId", this.auditSection.getAuditReport().getId());
	}
}
