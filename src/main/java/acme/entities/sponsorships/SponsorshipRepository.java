
package acme.entities.sponsorships;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.donations.Donation;

@Repository
public interface SponsorshipRepository extends AbstractRepository {

	@Query("SELECT SUM(d.money.amount) FROM Donation d WHERE d.sponsorship.id = :sponsorshipId")
	Money sumMoneyDonation(int sponsorshipId);

	Sponsorship findSponsorShipByTicker(String ticker);

	@Query("SELECT COUNT(d) FROM Donation d WHERE d.sponsorship.id = :sponsorshipId")
	long countDonationsBySponsorShipId(int sponsorshipId);

	@Query("SELECT d FROM Donation d WHERE d.sponsorship.id = :sponsorshipId")
	List<Donation> findDonationsBySponsorShipId(int sponsorshipId);
}
