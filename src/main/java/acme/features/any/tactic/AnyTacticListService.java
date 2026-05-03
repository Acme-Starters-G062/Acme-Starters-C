
package acme.features.any.tactic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.entities.tactic.Tactic;

@Service
public class AnyTacticListService extends AbstractService<Any, Tactic> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyTacticRepository	repository;
	private Strategy			strategy;
	private Collection<Tactic>	tactic;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int strategyId;
		strategyId = super.getRequest().getData("id", int.class);
		this.strategy = this.repository.findStrategyById(strategyId);
		this.tactic = this.repository.findPublishedTacticsByStrategyId(strategyId);
	}

	@Override
	public void authorise() {
		boolean status;
		status = this.strategy != null && !this.strategy.getDraftMode();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.tactic, "id", "name", "notes", "expectedPercentaje", "kind");
	}
}
