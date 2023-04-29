package itstep.learning.ioc;

import com.google.inject.AbstractModule;
import itstep.learning.data.DataContext;
import itstep.learning.service.*;

public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IHashService.class).to(Md5HashService.class);
        bind(IDBService.class).to(LocalMySqlDB.class);
        bind(IAuthService.class).to(SessionAuthService.class);
        bind(DataContext.class);
    }
}
