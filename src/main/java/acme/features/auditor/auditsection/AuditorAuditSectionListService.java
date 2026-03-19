
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

	private AuditReport						auditorReport;

	private Collection<AuditSection>		auditSections;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int auditReportId = super.getRequest().getData("auditReportId", int.class);

		this.auditorReport = this.repository.findAuditReportById(auditReportId);
		this.auditSections = this.repository.findAuditSectionsByAuditReportId(auditReportId);
	}

	@Override
	public void authorise() {

		boolean condition1 = this.auditorReport != null;
		boolean condition2 = !this.auditorReport.getDraftMode();
		boolean condition3 = this.auditorReport.getAuditor().isPrincipal();

		boolean status = condition1 && (condition2 || condition3);

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.auditSections, "name", "hours", "kind");

		boolean showCreate = this.auditorReport.getDraftMode() && this.auditorReport.getAuditor().isPrincipal();

		super.unbindGlobal("auditReportId", this.auditorReport.getId());
		super.unbindGlobal("showCreate", showCreate);
	}

}
