
package acme.features.inventor.invention;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.realms.Inventor;

@Service
public class InventorInventionPublishService extends AbstractService<Inventor, Invention> {

	@Autowired
	private InventorInventionRepository	repository;

	private Invention					invention;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id;

		id = super.getRequest().getData("id", int.class);
		this.invention = this.repository.findInventionById(id);
	}

	@Override
	public void authorise() {
		boolean status;
		status = this.invention != null && this.invention.getDraftMode() && this.invention.getInventor().isPrincipal();
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
			boolean hasParts;
			hasParts = this.repository.findCountPartsByInventionId(this.invention.getId()) > 0 && this.repository.findCountPartsByInventionId(this.invention.getId()) != null;
			super.state(hasParts, "*", "acme.validation.invention.no-parts.message");
		}
		{
			if (this.invention.getStartMoment() != null && this.invention.getEndMoment() != null) {
				boolean isFutureInterval = MomentHelper.isAfter(this.invention.getEndMoment(), this.invention.getStartMoment()) && MomentHelper.isFuture(this.invention.getStartMoment()) && MomentHelper.isFuture(this.invention.getEndMoment());
				super.state(isFutureInterval, "*", "acme.validation.invention.invalid-future-interval.message");
			}
		}
	}

	@Override
	public void execute() {
		this.invention.setDraftMode(false);
		this.repository.save(this.invention);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.invention, "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode");
	}

}
