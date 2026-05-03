
package acme.features.any.donation;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.donations.Donation;
import acme.entities.sponsorships.Sponsorship;

@Repository
public interface AnyDonationRepository extends AbstractRepository {

	@Query("select d from Donation d where d.id = :id")
	Donation findDonationById(@Param("id") int id);

	@Query("select s from Sponsorship s where s.id = :sponsorshipId")
	Sponsorship findSponsorshipById(int sponsorshipId);

	@Query("select d from Donation d where d.sponsorship.id = :sponsorshipId and d.sponsorship.draftMode = false")
	Collection<Donation> findPublishedDonationsBySponsorshipId(@Param("sponsorshipId") int sponsorshipId);
}
