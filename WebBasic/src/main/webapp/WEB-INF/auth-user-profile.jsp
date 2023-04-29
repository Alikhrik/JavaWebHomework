<%@ page import="itstep.learning.data.entity.User" %>
<%@ page import="itstep.learning.data.entity.User" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 26.03.2023
  Time: 15:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String contextPath = request.getContextPath();
    User user = (User) request.getAttribute("profile");
%>
<style>
    .user-value-box {
        display: flex;
        justify-content: space-between;
    }
    .user-value {
        display: flex;
        flex-direction: column;
    }
    .user-value-btn {
        display: flex;
        align-items: center;
    }
</style>
<div class="row">
    <div class="col l8 m10 s12">
        <h2 class="header">My profile</h2>
        <div class="card horizontal">
            <div class="card-image">
                <label for="input-avatar">
                    <img style="max-width: 90%; max-height: 90%; padding: 5px; border-radius: 15px; background: #00796b;
                     margin: 10px;" id="user-avatar"
                         src="<%= contextPath %>/image/<%= user.getAvatar() == null
                                                       ? "no-avatar.png" : user.getAvatar() %>">
                </label>
                <input id="input-avatar" hidden type="file">
            </div>
            <div class="card-stacked">
                <div class="card-content" style="display: flex; flex-wrap: wrap; flex-direction: column;">
                    <div class="user-value-box">
                        <div class="user-value">
                            <label for="user-login"><b>Login</b></label>
                            <input id="user-login" value="<%= user.getLogin() %>" disabled/>
                        </div>
                    </div>
                    <div class="user-value-box">
                        <div class="user-value">
                            <label for="user-name"><b>Real Name</b></label>
                            <input id="user-name" value="<%= user.getName() %>" disabled/>
                        </div>
                        <div class="user-value-btn">
                            <a id="user-name-btn" class="waves-effect waves-light btn-small">
                                <i class="material-icons">edit</i>
                            </a>
                        </div>
                    </div>
                    <div class="user-value-box">
                        <div class="user-value">
                            <label for="user-email"><b>Email</b></label>
                            <input id="user-email" value="<%= user.getEmail() %>" disabled/>
                        </div>
                        <div class="user-value-btn">
                            <a id="user-email-btn" class="waves-effect waves-light btn-small">
                                <i class="material-icons">edit</i>
                            </a>
                        </div>
                    </div>
                    <div style="display: flex; flex-direction: column;">
                        <div>
                            <label><b>Old Password</b></label>
                            <input type="password" id="user-old-pass">
                        </div>
                        <div>
                            <label><b>New Password</b></label>
                            <input type="password" id="user-new-pass">
                        </div>
                        <div>
                            <label><b>Repeat the password</b></label>
                            <input type="password" id="user-rep-pass">
                        </div>
                        <a id="user-pass-btn" class="waves-effect waves-light btn-small" disabled>
                            <i class="material-icons">check</i>
                        </a>
                    </div>
                    </br>
                    <span><b>Registration Data: </b> <i><%= user.getRegDt() %></i></span></br>
                </div>
                <div class="card-action">
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const userNameBtn = document.getElementById("user-name-btn");
        const userEmailBtn = document.getElementById("user-email-btn");
        const avatarInput = document.getElementById("input-avatar");
        const userName = document.getElementById("user-name");
        const userEmail = document.getElementById("user-email");

        const userOldPass = document.getElementById("user-old-pass");
        const userNewPass = document.getElementById("user-new-pass");
        const userRepPass = document.getElementById("user-rep-pass");
        const userPassBtn = document.getElementById("user-pass-btn");

        userName.setAttribute("old-value", userName.value);
        userEmail.setAttribute("old-value", userEmail.value);

        userNameBtn.addEventListener("click", userNameBtnClick);
        userEmailBtn.addEventListener("click", userEmailBtnClick);
        avatarInput.addEventListener("change", changeAvatar);

        userOldPass.addEventListener("change", changeTextEvent);
        userNewPass.addEventListener("change", changeTextEvent);
        userRepPass.addEventListener("change", changeTextEvent);
        userPassBtn.addEventListener("click", userPassBtnClick);
    });

    function userNameBtnClick(e) {
        const userName = document.getElementById("user-name");
        if ( userName.getAttribute('disabled') == null) {
            if( userName.getAttribute("old-value") != userName.value) {
                fetch("<%= contextPath %>/profile/?update=name", {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: `{ "user-name": "${userName.value}" }`
                }).then(r => r.text()).then(resp => {
                    if(resp == "OK") {
                        e.target.innerHTML = '<i class="material-icons">edit</i>';
                        userName.setAttribute('disabled', '');
                        userName.removeAttribute("old-value");
                        userName.setAttribute("old-value", userName.value);
                        M.toast({html: "name changed"});
                    } else {
                        M.toast({html: resp});
                    }
                });
            } else {
                e.target.innerHTML = '<i class="material-icons">edit</i>';
                userName.setAttribute('disabled', '');
            }
        } else {
            userName.removeAttribute('disabled');
            userName.focus();
            e.target.innerHTML = '<i class="material-icons">save</i>';
        }
    }

    function userEmailBtnClick(e) {
        const userEmail = document.getElementById("user-email");
        if ( userEmail.getAttribute('disabled') == null) {
            if( userEmail.getAttribute("old-value") !== userEmail.value ) {
                fetch("<%= contextPath %>/profile/?update=email", {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: `{ "user-email": "${userEmail.value}" }`
                }).then(r => r.text()).then(resp => {
                    if(resp == "OK") {
                        e.target.innerHTML = '<i class="material-icons">edit</i>';
                        userEmail.setAttribute('disabled', '');
                        userEmail.removeAttribute("old-value");
                        userEmail.setAttribute("old-value", userEmail.value);
                        M.toast({html: "email changed"});
                    } else {
                        M.toast({html: resp});
                    }
                });
            } else {
                e.target.innerHTML = '<i class="material-icons">edit</i>';
                userEmail.setAttribute('disabled', '');
            }
        } else {
            userEmail.removeAttribute('disabled');
            userEmail.focus();
            e.target.innerHTML = '<i class="material-icons">save</i>';
        }
    }

    const fileTypes = [
        "image/jpeg",
        "image/gif",
        "image/png"
    ]

    function changeAvatar(e) {
        const authProfileAvatar = document.getElementById("auth-profile-avatar");
        const avatar = document.getElementById("user-avatar");
        const file = e.target.files[0];
        if ( file.type != undefined && fileTypes.includes(file.type) ) {
            let formData = new FormData();
            formData.append("user-avatar", file);
            fetch("<%= contextPath %>/profile/?update=avatar", {
                method: "PUT",
                body: formData
            }).then(r => r.text()).then(resp => {
                if(resp == "OK") {
                    const newAvatar = URL.createObjectURL(file);
                    avatar.src = newAvatar;
                    authProfileAvatar.src = newAvatar;
                    M.toast({html: "avatar changed"});
                } else {
                    M.toast({html: resp});
                }
            });
        } else {
            M.toast({html: `File name ${file.name} not a valid file type`});
        }
    }

    function isValidPass( oldPass, newPass, repPass ) {
        return oldPass.value !== '' && newPass.value !== '' && repPass.value !== '';
    }

    function changeTextEvent() {
        const userOldPass = document.getElementById("user-old-pass");
        const userNewPass = document.getElementById("user-new-pass");
        const userRepPass = document.getElementById("user-rep-pass");
        const userPassBtn = document.getElementById("user-pass-btn");

        if( isValidPass( userOldPass, userNewPass, userRepPass ) ) {
            userPassBtn.removeAttribute('disabled');
        } else {
            userPassBtn.setAttribute('disabled', '');
        }
    }

    function userPassBtnClick() {
        const userOldPass = document.getElementById("user-old-pass");
        const userNewPass = document.getElementById("user-new-pass");
        const userRepPass = document.getElementById("user-rep-pass");
        const userPassBtn = document.getElementById("user-pass-btn");

        if( isValidPass( userOldPass, userNewPass, userRepPass ) ) {
            fetch("<%= contextPath %>/profile/?update=pass", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: `{
                     "old-pass": "${userOldPass.value}",
                     "new-pass": "${userNewPass.value}",
                     "rep-pass": "${userRepPass.value}"
                }`
            }).then(r => r.text()).then(resp => {
                if(resp == "OK") {
                    userOldPass.value = '';
                    userNewPass.value = '';
                    userRepPass.value = '';
                    M.toast({html: "password changed"});
                } else {
                    M.toast({html: resp});
                }
            });
        } else {
            userPassBtn.setAttribute('disabled', '');
        }
    }
</script>