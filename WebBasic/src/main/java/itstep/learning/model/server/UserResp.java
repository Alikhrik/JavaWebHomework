package itstep.learning.model.server;

import itstep.learning.data.entity.User;

import java.util.Date;
import java.util.UUID;

public class UserResp {
    private UUID id;
    private String login;
    private String name;
    private String email;
    private Date regDt;
    private String avatar;
    private String roleId;

    public UserResp( User entity ) {
        this.id = entity.getId();
        this.login = entity.getLogin();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.regDt = entity.getRegDt();
        this.avatar = entity.getAvatar();
        this.roleId = entity.getRoleId();
    }

    public UUID getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Date getRegDt() {
        return regDt;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getRoleId() {
        return roleId;
    }
}
