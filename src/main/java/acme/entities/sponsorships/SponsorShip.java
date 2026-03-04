
package acme.entities.sponsorships;

import java.time.Duration;
import java.util.List;

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
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoment.Constraint;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidHeader;
import acme.constraints.ValidSponsorShip;
import acme.constraints.ValidText;
import acme.constraints.ValidTicker;
import acme.entities.donations.Donation;
import acme.realms.Sponsor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidSponsorShip
public class SponsorShip extends AbstractEntity {

	// Serialisation version ---------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes --------------------------------------------------------

	@Autowired
	@Transient
	private SponsorShipRepository	sponsorShipRepository;

	@Mandatory
	@Column(unique = true)
	@ValidTicker
	private String					ticker;

	@Mandatory
	@Column
	@ValidHeader
	private String					name;

	@Mandatory
	@Column
	@ValidText
	private String					description;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	private Moment					startMoment;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(constraint = Constraint.ENFORCE_FUTURE)
	private Moment					endMoment;

	@Optional
	@Column
	@ValidUrl
	private String					moreInfo;

	@Mandatory
	@Column
	@Valid
	private Boolean					draftMode;

	@Valid
	@Transient
	@Mandatory
	private Double					monthsActive;

	@ValidMoney(min = 0.1)
	@Mandatory
	@Transient
	private Money					totalMoney;

	// Derived attributes -----------------------------------------------------


	public Double getMonthsActive() {
		Duration duration = Duration.between(this.startMoment.toInstant(), this.endMoment.toInstant());
		Long days = duration.toDays();
		Double months = days / 30.0;
		return Math.round(months) * 1.0;
	}

	public Money getTotalMoney() {
		List<Donation> donations = this.sponsorShipRepository.findDonationsBySponsorShipId(this.getId());

		Money total = new Money();
		total.setCurrency("EUR");
		total.setAmount(donations.stream().filter(d -> "EUR".equals(d.getMoney().getCurrency())).mapToDouble(d -> d.getMoney().getAmount()).sum());

		return total;
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Sponsor sponsor;
}
