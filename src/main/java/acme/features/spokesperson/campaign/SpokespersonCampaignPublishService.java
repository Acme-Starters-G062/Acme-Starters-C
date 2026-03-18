
package acme.features.spokesperson.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.campaigns.Campaign;
import acme.realms.Spokesperson;

@Service
public class SpokespersonCampaignPublishService extends AbstractService<Spokesperson, Campaign> {

	@Autowired
	private SpokespersonCampaignRepository	repository;

	private Campaign						campaign;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.campaign = this.repository.findCampaignById(id);
	}

	@Override
	public void authorise() {
		boolean status;
		status = this.campaign != null && this.campaign.getDraftMode() && this.campaign.getSpokesperson().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.campaign);

		{
			boolean hasMilestones;
			hasMilestones = this.repository.findCountMilestonesByCampaignId(this.campaign.getId()) > 0 && this.repository.findCountMilestonesByCampaignId(this.campaign.getId()) != null;
			super.state(hasMilestones, "*", "acme.validation.campaign.no-miletones.message");
		}
		{
			if (this.campaign.getStartMoment() != null && this.campaign.getEndMoment() != null) {
				boolean correctFutureInterval = MomentHelper.isAfter(this.campaign.getEndMoment(), this.campaign.getStartMoment()) && MomentHelper.isFuture(this.campaign.getStartMoment()) && MomentHelper.isFuture(this.campaign.getEndMoment());
				super.state(correctFutureInterval, "*", "acme.validation.campaign.invalid-future-date-interval.message");
			}
		}
	}

	@Override
	public void execute() {
		this.campaign.setDraftMode(false);
		this.repository.save(this.campaign);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.campaign, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}

}
