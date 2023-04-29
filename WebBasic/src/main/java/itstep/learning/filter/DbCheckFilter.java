package itstep.learning.filter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.service.IDBService;

import javax.servlet.*;
import java.io.IOException;

@Singleton
public class DbCheckFilter implements Filter {
    @Inject
    private IDBService dbService ;
    private FilterConfig filterConfig ;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig ;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        System.out.println("DbCheckFilter");
        try {
            dbService.getConnection() ;    // проверка подключения
            filterChain.doFilter( servletRequest, servletResponse ) ;
        }
        catch( Exception ex ) {
            System.err.println( ex.getMessage() ) ;
            servletRequest
                    .getRequestDispatcher( "WEB-INF/no-db.jsp" )
                    .forward( servletRequest, servletResponse ) ;
        }
    }

    @Override
    public void destroy() {
        filterConfig = null ;
    }
}