
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
			{
				boolean publishedWithAuditSection;

				publishedWithAuditSection = auditReport.getDraftMode() || this.repository.getAuditSections(auditReport.getId()).size() >= 1;

				super.state(context, publishedWithAuditSection, "*", "acme.validation.audit-report.published-without-audit-section.message");
			}
			{
				boolean startBeforeEnd;

				startBeforeEnd = MomentHelper.isBeforeOrEqual(auditReport.getStartMoment(), auditReport.getEndMoment());

				super.state(context, startBeforeEnd, "endMoment", "acme.validation.audit-report.start-before-end.message");
			}
			result = !super.hasErrors(context);
		}

		return result;
	}

}
