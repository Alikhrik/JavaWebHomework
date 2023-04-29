package itstep.learning.filter;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Singleton
public class ConfigFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String configPath = "C:\\Users\\user\\source\\repos\\JavaWebHomework\\WebBasic\\src\\main\\resources\\config.xml";
        HttpSession session = request.getSession();
        Properties props = new Properties();

        if( session.getAttribute("task-date-format") == null ) {
            props.loadFromXML( Files.newInputStream( Paths.get( configPath ) ) );
            session.setAttribute( "task-date-format", props.getProperty("task-date-format") );
        }
        filterChain.doFilter( servletRequest, servletResponse ) ;
    }

    @Override
    public void destroy() {

    }
}
