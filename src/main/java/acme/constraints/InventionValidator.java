
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.invention.Invention;
import acme.entities.invention.InventionRepository;

@Validator
public class InventionValidator extends AbstractValidator<ValidInvention, Invention> {

	@Autowired
	private InventionRepository repository;


	@Override
	public void initialise(final ValidInvention annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Invention invention, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;

		if (invention == null)
			result = true;
		else {
			{
				boolean uniqueInvention;

				Invention existingInvention;

				existingInvention = this.repository.findInventionByTicker(invention.getTicker());
				uniqueInvention = existingInvention == null || existingInvention.equals(invention);

				super.state(context, uniqueInvention, "ticker", "acme.validation.invention.duplicated-ticker.message");
			}
			{
				boolean correctNumPart;

				correctNumPart = this.repository.sumPartByInventionId(invention.getId()) > 0;

				super.state(context, correctNumPart, "parts", "acme.validation.invention.min-num-parts.message");
			}
			{
				boolean endIsAfterStart;

				Date now = MomentHelper.getCurrentMoment();
				endIsAfterStart = MomentHelper.isAfter(invention.getStartMoment(), invention.getEndMoment()) && MomentHelper.isAfter(now, invention.getStartMoment());
				;

				super.state(context, endIsAfterStart, "correctDate", "acme.validation.invention.correctDate.message");
			}
			{
				boolean eurCurrency;

				eurCurrency = this.repository.findPartsByInventionId(invention.getId()).stream().allMatch(p -> "EUR".equals(p.getCost().getCurrency()));

				super.state(context, eurCurrency, "money.currency", "acme.validation.invention.eur-currency.message");
			}
			result = !super.hasErrors(context);
		}
		return result;
	}
}
