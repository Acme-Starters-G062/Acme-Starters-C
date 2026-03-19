
package acme.features.auditor.auditreport;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Tuple;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.auditreport.AuditReport;
import acme.entities.auditsection.AuditSection;
import acme.realms.auditor.Auditor;

@Service
public class AuditorAuditReportPublishService extends AbstractService<Auditor, AuditReport> {

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

		super.validateObject(this.auditReport);
		{
			Collection<AuditSection> auditSections = this.repository.findAuditSectionsByAuditReportId(this.auditReport.getId());

			boolean hasAuditSectionts = auditSections != null && !auditSections.isEmpty();
			super.state(hasAuditSectionts, "hours", "acme.validation.auditReport.auditSections.error.message");
		}

		{
			Date start = this.auditReport.getStartMoment();
			Date end = this.auditReport.getEndMoment();

			boolean validInterval = start != null && end != null && MomentHelper.isAfter(end, start);

			super.state(validInterval, "startMoment", "acme.validation.auditReport.dates.error");
		}
		{
			Date now = MomentHelper.getCurrentMoment();
			Date start = this.auditReport.getStartMoment();
			Date end = this.auditReport.getEndMoment();

			boolean startInFuture = start != null && MomentHelper.isAfter(start, now);
			super.state(startInFuture, "startMoment", "acme.validation.auditReport.startMoment.future");

			boolean endInFuture = end != null && MomentHelper.isAfter(end, now);
			super.state(endInFuture, "endMoment", "acme.validation.auditReport.endMoment.future");
		}
	}

	@Override
	public void execute() {
		this.auditReport.setDraftMode(false);
		this.repository.save(this.auditReport);
	}

	@Override
	public void unbind() {
		Tuple tuple = super.unbindObject(this.auditReport, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");

		tuple.put("draftMode", this.auditReport.getDraftMode());
		tuple.put("monthsActive", this.auditReport.getMonthsActive());
		tuple.put("hours", this.auditReport.getHours());

	}

}
