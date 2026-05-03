
package acme.features.auditor.auditsection;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.auditreport.AuditReport;
import acme.entities.auditsection.AuditSection;
import acme.realms.auditor.Auditor;

@Service
public class AuditorAuditSectionListService extends AbstractService<Auditor, AuditSection> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditSectionRepository	repository;

	private AuditReport						auditReport;

	private Collection<AuditSection>		auditSections;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int auditReportId = super.getRequest().getData("auditReportId", int.class);

		this.auditReport = this.repository.findAuditReportById(auditReportId);
		this.auditSections = this.repository.findAuditSectionsByAuditReportId(auditReportId);
	}

	@Override
	public void authorise() {

		int auditorId = super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean isOwner = this.auditReport != null && this.auditReport.getAuditor().getId() == auditorId;

		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Auditor.class) && isOwner;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.auditSections, "name", "hours", "kind");

		boolean showCreate = this.auditReport.getDraftMode() && this.auditReport.getAuditor().isPrincipal();

		super.unbindGlobal("auditReportId", this.auditReport.getId());
		super.unbindGlobal("showCreate", showCreate);
	}

}
