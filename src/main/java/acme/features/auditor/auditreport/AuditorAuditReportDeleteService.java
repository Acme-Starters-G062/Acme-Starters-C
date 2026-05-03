
package acme.features.auditor.auditreport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.auditreport.AuditReport;
import acme.entities.auditsection.AuditSection;
import acme.realms.auditor.Auditor;

@Service
public class AuditorAuditReportDeleteService extends AbstractService<Auditor, AuditReport> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditReportRepository	repository;

	private AuditReport						auditReport;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);

		this.auditReport = this.repository.findAuditReportById(id);
	}

	@Override
	public void authorise() {
		int auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean isDraft = this.auditReport != null && this.auditReport.getDraftMode();
		boolean isOwner = this.auditReport != null && this.auditReport.getAuditor().getId() == auditorId;

		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Auditor.class) && isOwner && isDraft;

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.auditReport, "id");
	}

	@Override
	public void validate() {
		super.state(this.auditReport != null, "*", "auditor.audit-report.error.not-found");
	}

	@Override
	public void execute() {
		Collection<AuditSection> auditSections = this.repository.findAuditSectionsByAuditReportId(this.auditReport.getId());

		this.repository.deleteAll(auditSections);
		this.repository.delete(this.auditReport);
	}

	@Override
	public void unbind() {
		super.getResponse().addGlobal("confirmation", "auditor.audit-report.delete.success");
	}
}
