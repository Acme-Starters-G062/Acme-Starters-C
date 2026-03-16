
package acme.features.any.spokesperson;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.Spokesperson;

@Repository
public interface AnySpokespersonRepository extends AbstractRepository {

	@Query("select c.spokesperson from Campaign c where c.id = :campaignId")
	Spokesperson findSpokespersonById(int campaignId);

}
