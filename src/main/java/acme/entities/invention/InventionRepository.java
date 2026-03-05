
package acme.entities.invention;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.part.Part;

@Repository
public interface InventionRepository extends AbstractRepository {

	@Query("select sum(p.cost.amount) from Part p where p.invention.id = :inventionId and p.cost.currency = 'EUR'")
	Money sumCostByInventionId(@Param("inventionId") Integer inventionId);

	Invention findInventionByTicker(String ticker);

	@Query("select count(p) from Part p where p.invention.id = :inventionId")
	Integer sumPartByInventionId(Integer inventionId);

	@Query("select p from Part p where p.invention.id= :inventionId")
	List<Part> findPartsByInventionId(Integer inventionId);
}
