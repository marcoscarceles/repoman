<%@ page import="java.time.format.DateTimeFormatter" %>
<table>
    <thead>
    <tr>
        <g:sortableColumn property="sha" title="SHA" params="${['org': bean.owner]}"/>
        <g:sortableColumn property="date" title="Commit Date" params="${['org': bean.owner]}"/>
        <g:sortableColumn property="message" title="Message" params="${['org': bean.owner]}"/>
    </tr>
    </thead>
    <tbody>
    <g:each in="${bean.commits}" var="commit" status="i">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
            <td am-field="sha"><a href="${commit.url}">${commit.sha}</a></td>
            <td am-field="date">${java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME.format(commit.date)}</td>
            <td am-field="message">${commit.message}</td>
        </tr>
    </g:each>
    </tbody>
</table>