<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'organization.label', default: 'Organization')}" />
        <title>Organization List | RepoMan</title>
    </head>
    <body>
        <div id="list-organization" class="content scaffold-list" role="main">
            <h1>Organization List</h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <f:table collection="${organizationList}" properties="${['avatar','name', 'repoCount']}"/>

            <div class="pagination">
                <g:paginate total="${organizationCount ?: 0}" />
            </div>
        </div>
    </body>
</html>