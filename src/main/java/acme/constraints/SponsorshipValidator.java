
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
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
	public boolean isValid(final Sponsorship sponsorship, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;
		if (sponsorship == null)
			result = true;
		else {
			{
				boolean uniqueSponsorShip;
				Sponsorship existingSponsorShip;

				existingSponsorShip = this.repository.findSponsorShipByTicker(sponsorship.getTicker());
				uniqueSponsorShip = existingSponsorShip == null || existingSponsorShip.equals(sponsorship);

				super.state(context, uniqueSponsorShip, "ticker", "acme.validation.sponsorship.duplicated-ticker.message");
			}
			{
				boolean oneDonationAtLeast;

				oneDonationAtLeast = Boolean.TRUE.equals(sponsorship.getDraftMode()) || this.repository.countDonationsBySponsorShipId(sponsorship.getId()) >= 1;

				super.state(context, oneDonationAtLeast, "*", "acme.validation.sponsorship.one-donation.message");
			}
			{
				boolean futureInterval;
				boolean isDraft = Boolean.TRUE.equals(sponsorship.getDraftMode());
				if (!isDraft && sponsorship.getStartMoment() != null && sponsorship.getEndMoment() != null) {
					boolean orderedMoments = MomentHelper.isAfter(sponsorship.getEndMoment(), sponsorship.getStartMoment());

					futureInterval = orderedMoments;

					super.state(context, futureInterval, "*", "acme.validation.sponsorship.future-interval.message");
				}
			}
			{
				boolean eurCurrency;

				eurCurrency = Boolean.TRUE.equals(sponsorship.getDraftMode()) || this.repository.findDonationsBySponsorShipId(sponsorship.getId()).stream().allMatch(d -> "EUR".equals(d.getMoney().getCurrency()));

				super.state(context, eurCurrency, "*", "acme.validation.sponsorship.eur-currency.message");
			}
			result = !super.hasErrors(context);
		}
		return result;
	}
}
