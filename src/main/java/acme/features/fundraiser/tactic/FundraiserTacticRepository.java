
package acme.features.fundraiser.tactic;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.strategy.Strategy;
import acme.entities.tactic.Tactic;

@Repository
public interface FundraiserTacticRepository extends AbstractRepository {

	@Query("SELECT s FROM Strategy s WHERE s.id = :id")
	Strategy findStrategyById(int id);

	@Query("SELECT t FROM Tactic t WHERE t.strategy.id = :id")
	Collection<Tactic> findTacticsByStrategyId(int id);

	@Query("SELECT t FROM Tactic t WHERE t.id = :id")
	Tactic findTacticById(int id);

}
