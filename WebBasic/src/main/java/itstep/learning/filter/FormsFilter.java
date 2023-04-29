package itstep.learning.filter;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class FormsFilter implements Filter {
    private FilterConfig filterConfig;
    @Override
    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterConfig) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String method = req.getMethod();

        String text = req.getParameter("text");
        if(text != null && !text.equals("")) {
            resp.sendRedirect(req.getContextPath() + "/formsProcessor") ;
        } else if(method.equals("POST")) {
            resp.sendRedirect(req.getRequestURI());
        }

        filterConfig.doFilter(request, response);
    }
}
