/*
 * AnyAuditReportRepository.java
 *
 * Copyright (C) 2012-2026 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.auditReport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.auditreport.AuditReport;

@Repository
public interface AnyAuditReportRepository extends AbstractRepository {

	@Query("SELECT a FROM AuditReport a WHERE a.draftMode = false")
	Collection<AuditReport> findPublishedAuditReports();

	@Query("SELECT a FROM AuditReport a WHERE a.id = :id")
	AuditReport findOneAuditReportById(int id);

}
