package itstep.learning.service;

import com.google.inject.Singleton;
import itstep.learning.data.entity.User;

import javax.servlet.http.HttpServletRequest;

@Singleton
public class SessionAuthService implements IAuthService {
    private User authUser;
    @Override
    public void authorise(HttpServletRequest request) {
        authUser = (User) request.getSession().getAttribute("authUser");
    }

    @Override
    public void logout(HttpServletRequest request) {
        authUser = null;
        request.getSession().removeAttribute("authUser");
    }

    @Override
    public User getAuthUser() {
        return authUser;
    }
}
