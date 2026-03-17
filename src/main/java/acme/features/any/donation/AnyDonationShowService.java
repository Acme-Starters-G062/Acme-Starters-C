
package acme.features.any.donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.donations.Donation;

@Service
public class AnyDonationShowService extends AbstractService<Any, Donation> {

	@Autowired
	private AnyDonationRepository	repository;

	private Donation				donation;


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.donation = this.repository.findDonationById(id);
	}

	@Override
	public void authorise() {
		boolean status;

		status = this.donation != null && !Boolean.TRUE.equals(this.donation.getSponsorship().getDraftMode());

		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.donation, "id", "name", "notes", "money", "kind", "sponsorship.id");
	}
}
