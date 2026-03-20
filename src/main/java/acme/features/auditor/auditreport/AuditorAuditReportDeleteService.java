
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

		boolean condition1 = this.auditReport != null;
		boolean condition2 = this.auditReport.getDraftMode();
		boolean condition3 = this.auditReport.getAuditor().isPrincipal();

		boolean status = condition1 && condition2 && condition3;

		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
	}

	@Override
	public void execute() {
		Collection<AuditSection> auditSections = this.repository.findAuditSectionsByAuditReportId(this.auditReport.getId());

		this.repository.deleteAll(auditSections);
		this.repository.delete(this.auditReport);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode", "monthsActive", "hours");
	}
}
