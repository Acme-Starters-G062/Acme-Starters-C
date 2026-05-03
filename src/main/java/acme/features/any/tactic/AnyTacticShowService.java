
package acme.features.any.tactic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.tactic.Tactic;

@Service
public class AnyTacticShowService extends AbstractService<Any, Tactic> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyTacticRepository	repository;
	private Tactic				tactic;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int tacticId;
		tacticId = super.getRequest().getData("id", int.class);
		this.tactic = this.repository.findTacticById(tacticId);
	}

	@Override
	public void authorise() {
		boolean status;
		status = this.tactic != null && !this.tactic.getStrategy().getDraftMode();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.tactic, "id", "name", "notes", "expectedPercentaje", "kind", "strategy.id");
	}
}
