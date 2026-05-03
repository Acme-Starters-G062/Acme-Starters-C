
package acme.features.any.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.strategy.Strategy;

@Service
public class AnyStrategyShowService extends AbstractService<Any, Strategy> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyStrategyRepository	repository;

	private Strategy				strategy;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);

		this.strategy = this.repository.findStrategyById(id);
	}

	@Override
	public void authorise() {
		boolean isPublished = this.strategy != null && !this.strategy.getDraftMode();

		super.setAuthorised(isPublished);
	}

	@Override
	public void unbind() {
		super.unbindObject(this.strategy, "id", "ticker", "name", "description", "startMoment", "endMoment", "moreInfo", "draftMode", "monthsActive", "expectedPercentaje", "fundraiser.id");

		super.getResponse().addGlobal("fundraiserId", this.strategy.getFundraiser().getId());
	}

}
