<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true">
	<acme:form-textbox code="any.strategy.form.label.ticker" path="ticker"/>
	<acme:form-textbox code="any.strategy.form.label.name" path="name"/>
	<acme:form-textarea code="any.strategy.form.label.description" path="description"/>
	<acme:form-moment code="any.strategy.form.label.startMoment" path="startMoment"/>
	<acme:form-moment code="any.strategy.form.label.endMoment" path="endMoment"/>
	<acme:form-url code="any.strategy.form.label.moreInfo" path="moreInfo"/>
	<acme:form-checkbox code="any.strategy.form.label.draftMode" path="draftMode"/>
	<acme:form-double code="any.strategy.form.label.monthsActive" path="monthsActive" readonly="true"/>
	<acme:form-double code="any.strategy.form.label.expectedPercentaje" path="expectedPercentaje" readonly="true"/>
	
	<acme:button code="any.strategy.form.button.tactics" action="/any/tactic/list?id=${id}"/>
	<acme:button code="any.strategy.form.button.fundraiser" action="/any/fundraiser/show?id=${id}"/>
</acme:form>