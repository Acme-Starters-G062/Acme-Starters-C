
package acme.entities.campaigns;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CampaignRepository {

	@Query("select sum(m.effort) from Milestone m where m.campaign.id = :campaignId")
	Double sumEffortByCampaignId(int campaignId);

}
