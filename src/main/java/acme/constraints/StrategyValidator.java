
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.strategy.Strategy;
import acme.entities.strategy.StrategyRepository;

public class StrategyValidator extends AbstractValidator<ValidStrategy, Strategy> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private StrategyRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidStrategy annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Strategy strategy, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (strategy == null)
			result = true;
		else {
			{
				boolean hasTactics = false;
				if (strategy.getDraftMode() != null)
					if (!strategy.getDraftMode()) {
						hasTactics = !this.repository.getTactics(strategy.getId()).isEmpty();
						super.state(context, hasTactics, "*", "acme.validation.strategy.published-without-one-tactic.message");
					}
			}

			{
				boolean startBeforeEnd = false;
				if (strategy.getStartMoment() != null && strategy.getEndMoment() != null)
					startBeforeEnd = MomentHelper.isAfter(strategy.getEndMoment(), strategy.getStartMoment());
				super.state(context, startBeforeEnd, "endMoment", "acme.validation.strategy.start-before-end.message");
			}
			result = !super.hasErrors(context);
		}

		return result;
	}
}
