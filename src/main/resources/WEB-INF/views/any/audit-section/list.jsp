<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.auditSection.list.label.name" path="name" width="25%"/>
	<acme:list-column code="any.auditSection.list.label.notes" path="notes" width="35%"/>
	<acme:list-column code="any.auditSection.list.label.kind" path="kind" width="20%"/>
</acme:list>