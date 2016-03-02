<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'repo.label', default: 'Repo')}" />
        <title>${repo.fullName} Repo | RepoMan</title>
    </head>
    <body>
        <div id="show-repo" class="content scaffold-show" role="main">
            <h1>${repo.fullName}</h1>
            <div><span class="popularity lable">Popularity</span><h2 class="popularity-value">${repo.popularity}</h2>
            %{--<g:if test="${flash.message}">--}%
            %{--<div class="message" role="status">${flash.message}</div>--}%
            %{--</g:if>--}%
            <f:display bean="repo" property="commits"/>
            </div>
        </div>
    </body>
</html>
