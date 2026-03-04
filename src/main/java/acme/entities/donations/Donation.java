
package acme.entities.donations;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.entities.sponsorships.SponsorShip;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Donation extends AbstractEntity {

	//Serialisation version ----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	//Attributes --------------------------------------------------------

	@Mandatory
	@Column
	@ValidHeader
	private String				name;

	@Mandatory
	@Column
	@ValidText
	private String				notes;

	@Mandatory
	@Column
	@ValidMoney(min = 0.1)
	private Money				money;

	@Mandatory
	@Column
	@Valid
	private DonationKind		kind;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private SponsorShip			sponsorship;
}
