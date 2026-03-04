
package acme.entities.sponsorships;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.datatypes.Money;
import acme.client.repositories.AbstractRepository;
import acme.entities.donations.Donation;

@Repository
public interface SponsorShipRepository extends AbstractRepository {

	@Query("SELECT SUM(d.money) FROM Donation d WHERE d.sponsorship.id = :sponsorshipId")
	Money sumMoneyDonation(int sponsorshipId);

	SponsorShip findSponsorShipByTicker(String ticker);

	@Query("SELECT COUNT(d) FROM Donation d WHERE d.sponsorShip.id = :sponsorshipId")
	long countDonationsBySponsorShipId(int sponsorshipId);

	@Query("SELECT d FROM Donation d WHERE d.sponsorship.id = :sponsorshipId")
	List<Donation> findDonationsBySponsorShipId(int sponsorshipId);
}
