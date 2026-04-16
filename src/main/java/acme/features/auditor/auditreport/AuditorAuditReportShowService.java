
package acme.features.auditor.auditreport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.services.AbstractService;
import acme.entities.auditreport.AuditReport;
import acme.realms.auditor.Auditor;

@Service
public class AuditorAuditReportShowService extends AbstractService<Auditor, AuditReport> {

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

		boolean isOwner = this.auditReport != null && this.auditReport.getAuditor().getId() == auditorId;

		boolean status = super.getRequest().getPrincipal().hasRealmOfType(Auditor.class) && isOwner;

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		Tuple tuple = super.unbindObject(this.auditReport, "id", "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");

		tuple.put("monthsActive", this.auditReport.getMonthsActive());
		tuple.put("hours", this.auditReport.getHours());
	}

}
