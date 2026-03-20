
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.donations.Donation;
import acme.entities.sponsorships.Sponsorship;
import acme.realms.Sponsor;

@Service
public class SponsorSponsorshipDeleteService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected SponsorSponsorshipRepository	repository;

	private Sponsorship						sponsorship;

	// AbstractService<Sponsor, Sponsorship> ---------------------------------


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
	}

	@Override
	public void execute() {
		Collection<Donation> donations;

		donations = this.repository.findDonationsBySponsorshipId(this.sponsorship.getId());
		this.repository.deleteAll(donations);
		this.repository.delete(this.sponsorship);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.sponsorship, "id", "version", "ticker", "name", "startMoment", "endMoment", "moreInfo", "description", "draftMode");
	}

}
