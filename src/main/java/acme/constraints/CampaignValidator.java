
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.campaigns.Campaign;
import acme.entities.campaigns.CampaignRepository;

@Validator
public class CampaignValidator extends AbstractValidator<ValidCampaign, Campaign> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CampaignRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidCampaign annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Campaign campaign, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (campaign == null)
			result = true;
		else {
			{
				boolean uniqueCampaign;
				Campaign existingCampaign;

				existingCampaign = this.repository.findCampaignByTicker(campaign.getTicker());
				uniqueCampaign = existingCampaign == null || existingCampaign.equals(campaign);

				super.state(context, uniqueCampaign, "ticker", "acme.validation.campaign.duplicated-ticker.message");
			}
			{
				boolean hasMilestones;
				hasMilestones = Boolean.TRUE.equals(campaign.getDraftMode()) || this.repository.findCountMilestonesByCampaignId(campaign.getId()) != null && this.repository.findCountMilestonesByCampaignId(campaign.getId()) > 0;
				super.state(context, hasMilestones, "*", "acme.validation.campaign.no-milestones.message");
			}
			{
				boolean correctInterval;
				boolean isDraft = Boolean.TRUE.equals(campaign.getDraftMode());
				if (!isDraft && campaign.getStartMoment() != null && campaign.getEndMoment() != null) {
					boolean orderedMoments = MomentHelper.isAfter(campaign.getEndMoment(), campaign.getStartMoment());

					correctInterval = orderedMoments;

					super.state(context, correctInterval, "*", "acme.validation.campaign.invalid-date-interval.message");
				}
			}
			result = !super.hasErrors(context);
		}

		return result;
	}

}
