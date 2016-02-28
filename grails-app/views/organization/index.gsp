<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organization.label', default: 'Organization')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div id="list-organization" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <f:table collection="${organizationList}" properties="${['avatar','name']}"/>

            <div class="pagination">
                <g:paginate total="${organizationCount ?: 0}" />
            </div>
        </div>
    </body>
</html>