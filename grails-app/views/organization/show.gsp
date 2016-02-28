<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organization.label', default: 'Organization')}" />
        <title>${organization.name} Organization | RepoMan</title>
    </head>
    <body>
        <div id="show-organization" class="content scaffold-show" role="main">
            <h1><f:display bean="organization" property="avatar"/>${organization.name}</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <f:table collection="${organization.repos}" properties="${['name','popularity']}"/>
        </div>
    </body>
</html>
