
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.auditreport.AuditReport;
import acme.entities.auditreport.AuditReportRepository;

@Validator
public class AuditReportValidator extends AbstractValidator<ValidAuditReport, AuditReport> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditReportRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidAuditReport annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AuditReport auditReport, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (auditReport == null)
			result = true;
		else {
			if (auditReport.getTicker() != null) {
				AuditReport existing = this.repository.findByTicker(auditReport.getTicker());

				boolean uniqueTicker = existing == null || existing.getId() == auditReport.getId();

				super.state(context, uniqueTicker, "ticker", "acme.validation.audit-report.ticker.unique.message");
			}

			{
				boolean publishedWithAuditSection;

				publishedWithAuditSection = Boolean.TRUE.equals(auditReport.getDraftMode()) || !this.repository.getAuditSections(auditReport.getId()).isEmpty();

				super.state(context, publishedWithAuditSection, "draftMode", "acme.validation.audit-report.published-without-audit-section.message");
			}

			{
				boolean correctInterval;

				boolean isDraft = Boolean.TRUE.equals(auditReport.getDraftMode());
				if (!isDraft && auditReport.getStartMoment() != null && auditReport.getEndMoment() != null) {
					boolean orderedMoments = MomentHelper.isAfter(auditReport.getEndMoment(), auditReport.getStartMoment());

					correctInterval = orderedMoments;

					super.state(context, correctInterval, "endMoment", "acme.validation.audit-report.start-before-end.message");
				}
			}
			result = !super.hasErrors(context);
		}

		return result;
	}

}
