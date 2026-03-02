
package acme.constraints;

import java.util.Date;

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
				hasMilestones = campaign.getDraftMode() || this.repository.findCountMilestonesByCampaignId(campaign.getId()) != null;
				super.state(context, hasMilestones, "draftMode", "acme.validation.campaign.no-milestones.message");
			}
			{
				boolean correctInterval;
				boolean isDraft = campaign.getDraftMode();
				if (!isDraft && campaign.getStartMoment() != null && campaign.getEndMoment() != null) {
					Date now = MomentHelper.getCurrentMoment();
					boolean futureStart = MomentHelper.isAfter(campaign.getStartMoment(), now);
					boolean orderedMoments = MomentHelper.isAfter(campaign.getEndMoment(), campaign.getStartMoment());

					correctInterval = futureStart && orderedMoments;

					super.state(context, correctInterval, "endMoment", "acme.validation.campaign.invalid-date-interval.message");
				}
			}
			result = !super.hasErrors(context);
		}

		return result;
	}

}
