<%@ page import="itstep.learning.data.entity.User" %>
<%@ page import="itstep.learning.data.entity.User" %>
<%@ page import="itstep.learning.data.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
    String domain = request.getContextPath();
    User authUser = (User) request.getSession().getAttribute("authUser");
%>

<h2>Task manager</h2>

<% if( authUser != null ) { %>
    <jsp:include page="tasks.jsp"/>
<% } else { %>
    <p>
        Увійдіть до системи для перегляду задач
    </p>
<% } %>

