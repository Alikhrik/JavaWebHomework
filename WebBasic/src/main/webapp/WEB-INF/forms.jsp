<%@ page import="itstep.learning.model.FormsModel" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 15.02.2023
  Time: 20:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
    FormsModel model = (FormsModel) request.getAttribute("data");
%>
<html>
<head>
    <title>Forms</title>
</head>
<body>
    <h1>Передача данных</h1>

    <h1>Post Form</h1>
    <form method="post">
        <label>String:</label>
        <input type="text" placeholder="Enter string" name="string">
        <br/>
        <label>Number:</label>
        <input type="number" placeholder="Select number" name="number">
        <br/>
        <label>Date:</label>
        <input type="date" placeholder="Enter date" name="date">
        <br/>
        <label>Color:</label>
        <input type="color" title="Select color" name="color">
        <br/>
        <button>Send Post</button>
    </form>

    <h1>Get Form</h1>
    <form method="get">
        <input type="hidden" name="isFromForm" value="true">
        <label>String:</label>
        <input type="text" placeholder="Enter string" name="string">
        <br/>
        <label>Number:</label>
        <input type="text" placeholder="Select number" name="number">
        <br/>
        <label>Date:</label>
        <input type="date" placeholder="Enter date" name="date">
        <br/>
        <label>Color:</label>
        <input type="color" title="Select color" name="date">
        <br/>
        <button>Send Get</button>
    </form>

    <p>
        Response: <%= model == null ? "not exist" : "\n" + model.toString() %>
    </p>
</body>
</html>
