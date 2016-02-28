<table>
    <thead>
    <tr>
        <g:sortableColumn property="sha" title="SHA" params="${['org': bean.owner]}"/>
        <g:sortableColumn property="message" title="Message" params="${['org': bean.owner]}"/>
    </tr>
    </thead>
    <tbody>
    <g:each in="${bean.commits}" var="commit" status="i">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td><a href="${commit.url}">${commit.sha}</a></td>
            <td>${commit.message}</td>
        </tr>
    </g:each>
    </tbody>
</table>