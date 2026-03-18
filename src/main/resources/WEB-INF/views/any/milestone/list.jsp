<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.milestone.list.label.title" path="title" width="40%"/>	
	<acme:list-column code="any.milestone.list.label.achievements" path="achievements" width="60%"/>
	<acme:list-hidden path="effort"/>
	<acme:list-hidden path="kind"/>
</acme:list>