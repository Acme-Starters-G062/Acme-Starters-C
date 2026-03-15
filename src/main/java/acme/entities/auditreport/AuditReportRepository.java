
package acme.entities.auditreport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.auditsection.AuditSection;

@Repository
public interface AuditReportRepository extends AbstractRepository {

	@Query("SELECT SUM(aus.hours) FROM AuditSection aus WHERE aus.auditReport.id = :id")
	Integer sumHoursAuditSections(int id);

	@Query("SELECT aus FROM AuditSection aus WHERE aus.auditReport.id = :id")
	Collection<AuditSection> getAuditSections(int id);

	@Query("SELECT a FROM AuditReport a WHERE a.ticker = :ticker")
	AuditReport findByTicker(String ticker);

}
