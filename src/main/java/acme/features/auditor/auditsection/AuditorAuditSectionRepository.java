
package acme.features.auditor.auditsection;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.auditreport.AuditReport;
import acme.entities.auditsection.AuditSection;

@Repository
public interface AuditorAuditSectionRepository extends AbstractRepository {

	@Query("SELECT a FROM AuditReport a WHERE a.id = :id")
	AuditReport findAuditReportById(int id);

	@Query("SELECT a FROM AuditSection a WHERE a.id = :id")
	AuditSection findAuditSectionById(int id);

	@Query("SELECT a FROM AuditSection a WHERE a.auditReport.id = :id")
	Collection<AuditSection> findAuditSectionsByAuditReportId(int id);

}
