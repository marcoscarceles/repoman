<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organization.label', default: 'Organization')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div id="show-organization" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>

            <div class="property-list organization">
                <f:display bean="organization" property="avatar"/>
                <f:display bean="organization" property="name"/>
                <f:display bean="organization" property="repos"/>
            </div>
        </div>
    </body>
</html>
