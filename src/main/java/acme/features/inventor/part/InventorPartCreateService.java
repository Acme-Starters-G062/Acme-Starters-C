
package acme.features.inventor.part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Tuple;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.entities.invention.Invention;
import acme.entities.part.Part;
import acme.entities.part.PartKind;
import acme.realms.Inventor;

@Service
public class InventorPartCreateService extends AbstractService<Inventor, Part> {

	@Autowired
	private InventorPartRepository	repository;

	private Part					part;


	@Override
	public void load() {
		int inventionId;
		Invention invention;

		inventionId = super.getRequest().getData("inventionId", int.class);
		invention = this.repository.findInventionById(inventionId);

		Money newCost = new Money();
		newCost.setAmount(0.01);
		newCost.setCurrency("EUR");

		this.part = super.newObject(Part.class);
		this.part.setName("");
		this.part.setDescription("");
		this.part.setCost(newCost);
		this.part.setInvention(invention);
	}

	@Override
	public void authorise() {
		boolean status;
		int inventionId;
		Invention invention;
		inventionId = super.getRequest().getData("inventionId", int.class);
		invention = this.repository.findInventionById(inventionId);
		status = invention != null && this.part.getInvention().getDraftMode() && this.part.getInvention().getInventor().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void bind() {
		super.bindObject(this.part, "name", "description", "cost", "kind");
	}

	@Override
	public void validate() {
		super.validateObject(this.part);
		super.state(this.part.getCost().getCurrency().equals("EUR"), "cost", "acme.validation.invention.eur-currency.message");
	}

	@Override
	public void execute() {
		this.repository.save(this.part);
	}

	@Override
	public void unbind() {
		Tuple tuple;
		SelectChoices choices;

		choices = SelectChoices.from(PartKind.class, this.part.getKind());

		tuple = super.unbindObject(this.part, "name", "description", "cost", "kind");
		tuple.put("inventionId", super.getRequest().getData("inventionId", int.class));
		tuple.put("draftMode", this.part.getInvention().getDraftMode());
		tuple.put("kinds", choices);
	}

}
