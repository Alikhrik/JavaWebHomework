package itstep.learning.filter;

import com.google.inject.Singleton;

import javax.servlet.*;
import java.io.IOException;


@Singleton
public class CharsetFilter implements Filter {
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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        filterConfig.doFilter(request, response);
    }
}
