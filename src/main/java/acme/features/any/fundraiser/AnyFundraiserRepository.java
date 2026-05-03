
package acme.features.any.fundraiser;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.strategy.Strategy;
import acme.realms.fundraiser.Fundraiser;

@Repository
public interface AnyFundraiserRepository extends AbstractRepository {

	@Query("SELECT s.fundraiser FROM Strategy s WHERE s.id = :id")
	Fundraiser findFundraiserByStrategyId(int id);

	@Query("SELECT s FROM Strategy s WHERE s.id = :id")
	Strategy findStrategyById(int id);
}
