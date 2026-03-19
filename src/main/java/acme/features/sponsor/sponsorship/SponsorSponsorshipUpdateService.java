
package acme.features.sponsor.sponsorship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipUpdateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected SponsorSponsorshipRepository	repository;

	private Sponsorship						sponsorship;

	// AbstractService interface ----------------------------------------------


	@Override
	public void load() {
		int id;
		id = super.getRequest().getData("id", int.class);
		this.sponsorship = this.repository.findOneById(id);
	}

	@Override
	public void authorise() {
		boolean status;
		status = this.sponsorship != null && this.sponsorship.getDraftMode() && this.sponsorship.getSponsor().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.sponsorship, "ticker", "name", "startMoment", "endMoment", "moreInfo", "description");
	}

	@Override
	public void validate() {
		super.validateObject(this.sponsorship);
		if (this.sponsorship.getEndMoment() != null && this.sponsorship.getStartMoment() != null)
			super.state(MomentHelper.isAfter(this.sponsorship.getEndMoment(), this.sponsorship.getStartMoment()), "endMoment", "acme.validation.sponsorship.end-after-start");
	}

	@Override
	public void execute() {
		this.repository.save(this.sponsorship);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.sponsorship, "id", "version", "ticker", "name", "startMoment", "endMoment", "moreInfo", "description", "draftMode");
	}

}
