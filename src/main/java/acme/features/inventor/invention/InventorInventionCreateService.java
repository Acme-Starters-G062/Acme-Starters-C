
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionCreateService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository	repository;

	private Invention					invention;


	@Override
	public void load() {
		Inventor inventor;

		inventor = (Inventor) super.getRequest().getPrincipal().getActiveRealm();

		this.invention = super.newObject(Invention.class);
		this.invention.setDraftMode(true);
		this.invention.setInventor(inventor);
	}

	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(Inventor.class);
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo");
	}

	@Override
	public void validate() {
		super.validateObject(this.invention);
		{
			if (this.invention.getStartMoment() != null && this.invention.getEndMoment() != null) {
				boolean correctDates = MomentHelper.isAfter(this.invention.getEndMoment(), this.invention.getStartMoment());
				super.state(correctDates, "*", "acme.validation.invention.invalid-dates.message");
			}
		}
	}

	@Override
	public void execute() {
		this.repository.save(this.invention);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}
}
