<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="spokesperson.campaign.list.label.ticker" path="ticker" width="20%"/>
	<acme:list-column code="spokesperson.campaign.list.label.name" path="name" width="20%"/>
	<acme:list-column code="spokesperson.campaign.list.label.description" path="description" width="60%"/>
	<acme:list-hidden path="startMoment"/>
	<acme:list-hidden path="endMoment"/>
	<acme:list-hidden path="moreInfo"/>
	<acme:list-hidden path="spokesperson.identity.fullName"/> 
</acme:list>

<acme:button code="spokesperson.campaign.list.button.create" action="/spokesperson/campaign/create"/>
