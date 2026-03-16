
package acme.entities.strategy;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.tactic.Tactic;

@Repository
public interface StrategyRepository extends AbstractRepository {

	@Query("SELECT SUM(t.expectedPercentaje) FROM Tactic t WHERE t.strategy.id = :id")
	Double sumPercentaje(int id);

	@Query("SELECT t FROM Tactic t WHERE t.strategy.id = :id")
	Collection<Tactic> getTactics(int id);

}
