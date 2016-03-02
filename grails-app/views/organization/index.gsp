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

            <form class="search-box" action="${g.createLink(action:'search-query')}">
                <input type="text" name="query" placeholder="Search for an Organization ..." class="input-search" autofocus required>
                <button type="submit" class="btn btn-success btn-search"><i class="fa fa-search"></i> Search</button>
                <p>... Or choose one from the table below:</p>
            </form>

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <f:table collection="${organizationList}" properties="${['avatar','name', 'description', 'repoCount']}"/>

            <div class="pagination">
                <g:paginate total="${organizationCount ?: 0}" />
            </div>
        </div>
    </body>
</html>