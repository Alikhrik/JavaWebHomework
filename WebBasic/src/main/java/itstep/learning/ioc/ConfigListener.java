package itstep.learning.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ConfigListener extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new RouterModule(),
                new ServiceModule(),
                new StringModule(),
                new LoggerModule(),
                new WebsocketModule()
        );
    }
}
