
package acme.features.fundraiser.strategy;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.strategy.Strategy;
import acme.entities.tactic.Tactic;

public interface FundraiserStrategyRepository extends AbstractRepository {

	@Query("SELECT s FROM Strategy s WHERE s.fundraiser.id = :id")
	Collection<Strategy> findStrategyByFundraiserId(int id);

	@Query("SELECT s FROM Strategy s WHERE s.id = :id")
	Strategy findStrategyById(int id);

	@Query("SELECT t FROM Tactic t WHERE t.strategy.id = :id")
	Collection<Tactic> findTacticsByStrategyId(int id);

}
