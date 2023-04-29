package itstep.learning.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.data.dao.UserDao;
import itstep.learning.data.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class AuthServlet extends HttpServlet {
    @Inject
    private UserDao userDao;
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("auth-login");
        String pass = req.getParameter("auth-pass");
        if(login.trim().length() > 0 && pass.trim().length() > 0) {
            User user = userDao.getUserByCredentials(login, pass);
            if(user != null) {
                req.getSession().setAttribute("authUser", user);
                resp.getWriter().print("OK");
                return;
            }
        }
        resp.getWriter().print("NO");
    }
}
