
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.sponsorships.SponsorShip;
import acme.entities.sponsorships.SponsorShipRepository;

@Validator
public class SponsorShipValidator extends AbstractValidator<ValidSponsorShip, SponsorShip> {

	@Autowired
	private SponsorShipRepository repository;


	@Override
	protected void initialise(final ValidSponsorShip annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final SponsorShip sponsorShip, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		if (sponsorShip == null)
			result = true;
		else {
			{
				boolean uniqueSponsorShip;
				SponsorShip existingSponsorShip;

				existingSponsorShip = this.repository.findSponsorShipByTicker(sponsorShip.getTicker());
				uniqueSponsorShip = existingSponsorShip == null || existingSponsorShip.equals(sponsorShip);

				super.state(context, uniqueSponsorShip, "ticker", "acme.validation.sponsorShip.duplicated-ticker.message");
			}
			{
				boolean oneDonationAtLeast;

				oneDonationAtLeast = sponsorShip.getDraftMode() || this.repository.countDonationsBySponsorShipId(sponsorShip.getId()) >= 1;

				super.state(context, oneDonationAtLeast, "donations", "acme.validation.sponsorShip.one-donation.message");
			}
			{
				boolean futureInterval;

				futureInterval = sponsorShip.getDraftMode()
					|| sponsorShip.getStartMoment().compareTo(MomentHelper.getCurrentMoment()) > 0 && sponsorShip.getEndMoment().compareTo(MomentHelper.getCurrentMoment()) > 0 && sponsorShip.getEndMoment().compareTo(sponsorShip.getStartMoment()) > 0;

				super.state(context, futureInterval, "moments", "acme.validation.sponsorShip.future-interval.message");
			}
			{
				boolean eurCurrency;

				eurCurrency = sponsorShip.getDraftMode() || this.repository.findDonationsBySponsorShipId(sponsorShip.getId()).stream().allMatch(d -> "EUR".equals(d.getMoney().getCurrency()));

				super.state(context, eurCurrency, "money.currency", "acme.validation.sponsorShip.eur-currency.message");
			}
			result = !super.hasErrors(context);
		}
		return result;
	}
}
