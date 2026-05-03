/*
 * AnyAuditorRepository.java
 *
 * Copyright (C) 2012-2026 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.auditsection;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.auditreport.AuditReport;
import acme.entities.auditsection.AuditSection;

@Repository
public interface AnyAuditSectionRepository extends AbstractRepository {

	@Query("SELECT ar FROM AuditReport ar WHERE ar.id = :id")
	AuditReport findAuditReportById(int id);

	@Query("SELECT s FROM AuditSection s WHERE s.id = :id")
	AuditSection findAuditSectionById(int id);

	@Query("SELECT s FROM AuditSection s WHERE s.auditReport.id = :id")
	Collection<AuditSection> findAuditSectionsByAuditReport(int id);
}
