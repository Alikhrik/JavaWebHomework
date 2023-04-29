package itstep.learning.ioc;

import com.google.inject.servlet.ServletModule;
import itstep.learning.filter.*;
import itstep.learning.servlet.*;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("/*").through(CharsetFilter.class);
        filterRegex("^/(?!image.*).*$").through(DbCheckFilter.class);
        // filter("/*").through(AuthFilter.class);
        filterRegex("^/(?!image.*).*$").through(AuthFilter.class);

        serveRegex("^(/home|/)$").with(HomeServlet.class);
        serve("/story").with(StoryServlet.class);
        serve("/auth").with(AuthServlet.class);
        serve("/about").with(AboutServlet.class);
        serve("/forms").with(FormsServlet.class);
        serve("/registration").with(UserRegisterServlet.class);
        serve("/image/*").with(DownloadServlet.class);
        serve("/profile/*").with((UserProfileServlet.class));
    }
}
