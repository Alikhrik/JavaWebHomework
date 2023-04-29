package itstep.learning.filter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.service.IAuthService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class AuthFilter implements Filter {
    @Inject
    private IAuthService authService;

    @Inject
    private Logger logger;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if(request.getParameter("logout") != null) {
            HttpServletResponse response = (HttpServletResponse)servletResponse;
            authService.logout(request);
            response.sendRedirect( request.getContextPath() + "/");
        } else {
            authService.authorise(request);
            request.setAttribute("authUser", authService.getAuthUser());
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
