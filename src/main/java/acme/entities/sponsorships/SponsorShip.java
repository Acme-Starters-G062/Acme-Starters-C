
package acme.entities.sponsorships;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.realms.Sponsor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SponsorShip extends AbstractEntity {

	// Serialisation version ---------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes --------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	//@ValidTicker
	private String				ticker;

	@Mandatory
	@Column
	//@ValidHeader
	private String				name;

	@Mandatory
	@Column
	//@ValidText
	private String				description;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	//@ValidMoment(future)
	private Moment				startMoment;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	//@ValidMoment(future)
	private Moment				endMoment;

	@Optional
	@Column
	//@ValidUrl
	private String				moreInfo;

	@Mandatory
	@Column
	@Valid
	private Boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne
	@Valid
	private Sponsor				sponsor;
}
