<%@ page import="itstep.learning.data.entity.User" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 26.03.2023
  Time: 13:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
    User user = (User) request.getAttribute("profile");
%>
<div class="row">
    <div class="col l8 m10 s12">
        <h2 class="header">Profile</h2>
        <div class="card horizontal">
            <div class="card-image">
                <img style="max-width: 90%; max-height: 90%; padding: 5px; border-radius: 15px; background: #00796b;
                     margin: 10px;"
                     src="<%= contextPath %>/image/<%= user.getAvatar() == null
                                                       ? "no-avatar.png" : user.getAvatar() %>">
            </div>
            <div class="card-stacked">
                <div class="card-content">
                    <span><b>Name: </b> <i><%= user.getName() %></i></span></br>
                    <span><b>Login: </b> <i><%= user.getLogin() %></i></span></br>
                    <span><b>Email: </b> <i><%= user.getEmail() %></i></span></br>
                    <span><b>Registration Data: </b> <i><%= user.getRegDt() %></i></span></br>
                    </i>
                </div>
                <div class="card-action">
                    <a href="#">This is a link</a>
                </div>
            </div>
        </div>
    </div>
</div>