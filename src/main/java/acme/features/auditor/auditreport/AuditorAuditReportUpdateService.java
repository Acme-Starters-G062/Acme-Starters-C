
package acme.features.auditor.auditreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.auditreport.AuditReport;
import acme.realms.auditor.Auditor;

@Service
public class AuditorAuditReportUpdateService extends AbstractService<Auditor, AuditReport> {

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
		super.bindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.auditReport);
	}

	@Override
	public void execute() {
		this.repository.save(this.auditReport);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode", "monthsActive", "hours");
	}

}
