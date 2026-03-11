
package acme.entities.auditsection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidHeader;
import acme.constraints.ValidText;
import acme.entities.auditreport.AuditReport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditSection extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Mandatory Attributes -------------------------------------------------------------

	//Atributes

	@Mandatory
	@ValidHeader
	@Column
	private String				name;

	@Mandatory
	@ValidText
	@Column
	private String				notes;

	@Mandatory
	@ValidNumber(min = 0)
	@Column
	private Integer				hours;

	@Mandatory
	@Valid
	@Column
	private SectionKind			kind;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AuditReport			auditReport;

}
