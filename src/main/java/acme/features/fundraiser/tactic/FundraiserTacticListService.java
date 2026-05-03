
package acme.features.fundraiser.tactic;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;
import acme.entities.tactic.Tactic;
import acme.realms.fundraiser.Fundraiser;

@Service
public class FundraiserTacticListService extends AbstractService<Fundraiser, Tactic> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected FundraiserTacticRepository	repository;

	private Strategy						strategy;
	private Collection<Tactic>				tactics;

	// AbstractService<Fundraiser, Tactic> ---------------------------------


	@Override
	public void load() {
		int strategyId;

		strategyId = super.getRequest().getData("strategyId", int.class);
		this.strategy = this.repository.findStrategyById(strategyId);
		this.tactics = this.repository.findTacticsByStrategyId(strategyId);
	}

	@Override
	public void authorise() {
		boolean status;
		status = this.strategy != null && this.strategy.getFundraiser().isPrincipal();
		super.setAuthorised(status);
	}

	@Override
	public void unbind() {
		super.unbindObjects(this.tactics, "name", "notes", "expectedPercentaje", "kind");

		boolean showCreate = this.strategy.getDraftMode() && this.strategy.getFundraiser().isPrincipal();

		super.unbindGlobal("strategyId", this.strategy.getId());
		super.unbindGlobal("showCreate", showCreate);
	}
}
