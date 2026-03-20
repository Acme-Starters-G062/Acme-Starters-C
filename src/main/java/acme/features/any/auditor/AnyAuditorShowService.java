/*
 * AnyAuditorShowService.java
 *
 * Copyright (C) 2012-2026 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.auditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractService;
import acme.realms.auditor.Auditor;

@Service
public class AnyAuditorShowService extends AbstractService<Any, Auditor> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyAuditorRepository	repository;

	private Auditor					auditor;

	// AbstractService interface -------------------------------------------


	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);

		this.auditor = this.repository.findAuditorById(id);
	}

	@Override
	public void authorise() {
		boolean status = this.auditor != null;

		super.setAuthorised(status);
	}

	private SelectChoices getSolicitorChoices(final Boolean selected) {
		SelectChoices result = new SelectChoices();

		result.add("false", "any.auditor.form.value.false", Boolean.FALSE.equals(selected));
		result.add("true", "any.auditor.form.value.true", Boolean.TRUE.equals(selected));

		return result;
	}

	@Override
	public void unbind() {
		SelectChoices solicitorChoices = this.getSolicitorChoices(this.auditor.getSolicitor());

		super.unbindObject(this.auditor, "firm", "highlights", "solicitor", "identity.name", "identity.surname", "identity.email");
		super.getResponse().addGlobal("solicitorChoices", solicitorChoices);
	}
}
