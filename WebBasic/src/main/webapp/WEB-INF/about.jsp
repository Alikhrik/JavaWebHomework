<%@ page import="itstep.learning.model.AboutModel" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 15.02.2023
  Time: 18:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
    AboutModel model = (AboutModel) request.getAttribute("data");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>About</h1>
<p>
    From servlet: <%= model.getMessage() %> <br/>
    <%= model.getMoment().toString() %>
</p>
</body>
</html>
