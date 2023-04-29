package itstep.learning.service;

import itstep.learning.data.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface IAuthService {
    void authorise(HttpServletRequest request);

    void logout(HttpServletRequest request);
    User getAuthUser();
}
