<%@ page import="itstep.learning.model.FormsModel" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 21.02.2023
  Time: 17:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    FormsModel model = (FormsModel) request.getAttribute("model");
%>
<html>
<head>
    <title>Forms Processor</title>
</head>
<body>
    <h1>Forms Processor</h1>
    <p>
        Response: <%= model == null ? "not exist" : "\n" + model.toString() %>
    </p>
</body>
</html>
