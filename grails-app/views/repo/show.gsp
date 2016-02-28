<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'repo.label', default: 'Repo')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div id="show-repo" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div class="property-list organization">
                <dl>
                    <dt class="name">Name</dt>
                    <dd class="name">${repo.fullName}</dd>

                    <dt class="popularity">Popularity</dt>
                    <dd class="popularity">${repo.popularity}</dd>

                    <dt class="commits">Latest Commits</dt>
                    <dd class="commits"><f:display bean="repo" property="commits"/></dd>
                </dl>
                %{--<f:table collection="${repo.commits}" properties="${['sha','message', 'url']}"/>--}%
            </div>
        </div>
    </body>
</html>
