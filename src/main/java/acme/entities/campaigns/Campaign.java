
package acme.entities.campaigns;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.realms.Spokesperson;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Campaign extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidTicker
	@Column(unique = true)
	private String				ticker;

	@Mandatory
	@ValidHeader
	@Column
	private String				name;

	@Mandatory
	@ValidText
	@Column
	private String				description;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Moment				startMoment;

	@Mandatory
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	@Temporal(TemporalType.TIMESTAMP)
	private Moment				endMoment;

	@Optional
	@ValidUrl
	@Column
	private String				moreInfo;

	@Mandatory
	@Valid
	@Transient
	private Double				monthsActive;

	@Mandatory
	@ValidNumber(min = 1)
	@Transient
	private Double				effort;

	@Transient
	@Autowired
	private CampaignRepository	repository;


	public Double getMonthsActive() {
		if (this.startMoment == null || this.endMoment == null)
			return 0.;
		long millis = this.endMoment.getTime() - this.startMoment.getTime();
		double days = millis / (1000.0 * 60.0 * 60.0 * 24.0);
		return Math.round(days / 30.0) * 1.0;
	}

	public Double getEffort() {
		double result;
		Double wrapper;
		wrapper = this.repository.sumEffortByCampaignId(this.getId());
		result = wrapper == null ? 0 : wrapper.doubleValue();
		return result;
	}


	@Mandatory
	@Valid
	@Column
	private Boolean			draftMode;

	@Mandatory
	@Valid
	@ManyToOne
	private Spokesperson	spokesperson;

}
