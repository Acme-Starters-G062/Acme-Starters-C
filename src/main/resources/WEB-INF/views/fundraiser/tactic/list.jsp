<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.tactic.list.label.name" path="name" width="20%"/>
	<acme:list-column code="any.tactic.list.label.expectedPercentaje" path="expectedPercentaje" width="25%"/>
	<acme:list-column code="any.tactic.list.label.kind" path="kind" width="20%"/>
</acme:list>

<jstl:if test="${showCreate == true}">
	<acme:button code="fundraiser.tactic.list.button.create" action="/fundraiser/tactic/create?strategyId=${strategyId}"/>
</jstl:if>