
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipRepository;

@Validator
public class SponsorshipValidator extends AbstractValidator<ValidSponsorShip, Sponsorship> {

	@Autowired
	private SponsorshipRepository repository;


	@Override
	protected void initialise(final ValidSponsorShip annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Sponsorship sponsorShip, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		if (sponsorShip == null)
			result = true;
		else {
			{
				boolean uniqueSponsorShip;
				Sponsorship existingSponsorShip;

				existingSponsorShip = this.repository.findSponsorShipByTicker(sponsorShip.getTicker());
				uniqueSponsorShip = existingSponsorShip == null || existingSponsorShip.equals(sponsorShip);

				super.state(context, uniqueSponsorShip, "ticker", "acme.validation.sponsorShip.duplicated-ticker.message");
			}
			{
				boolean oneDonationAtLeast;

				oneDonationAtLeast = Boolean.TRUE.equals(sponsorShip.getDraftMode()) || this.repository.countDonationsBySponsorShipId(sponsorShip.getId()) >= 1;

				super.state(context, oneDonationAtLeast, "*", "acme.validation.sponsorShip.one-donation.message");
			}
			{
				boolean futureInterval;

				futureInterval = Boolean.TRUE.equals(sponsorShip.getDraftMode()) || sponsorShip.getEndMoment().compareTo(sponsorShip.getStartMoment()) > 0;

				super.state(context, futureInterval, "*", "acme.validation.sponsorShip.future-interval.message");
			}
			{
				boolean eurCurrency;

				eurCurrency = Boolean.TRUE.equals(sponsorShip.getDraftMode()) || this.repository.findDonationsBySponsorShipId(sponsorShip.getId()).stream().allMatch(d -> "EUR".equals(d.getMoney().getCurrency()));

				super.state(context, eurCurrency, "*", "acme.validation.sponsorShip.eur-currency.message");
			}
			result = !super.hasErrors(context);
		}
		return result;
	}
}
