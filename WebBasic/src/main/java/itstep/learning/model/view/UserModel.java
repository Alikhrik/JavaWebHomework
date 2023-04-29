package itstep.learning.model.view;

import javax.servlet.http.HttpServletRequest;

public class UserModel {
    private String login ;
    private String pass1 ;
    private String pass2 ;
    private String name ;
    private String email ;
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static UserModel parseModel(HttpServletRequest request) {
        UserModel model = new UserModel();
        model.setLogin( request.getParameter( "user-login" ) ) ;
        model.setPass1( request.getParameter( "user-pass1" ) ) ;
        model.setPass2( request.getParameter( "user-pass2" ) ) ;
        model.setName(  request.getParameter( "user-name"  ) ) ;
        model.setEmail( request.getParameter( "user-email" ) ) ;
        return model;
    }
}