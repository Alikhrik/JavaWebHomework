package itstep.learning.data.dao;

import itstep.learning.service.IDBService;

import java.util.logging.Logger;


public class Dao {
    protected final IDBService dbService;
    protected final Logger logger;

    public Dao( IDBService dbService, Logger logger ) {
        this.dbService = dbService;
        this.logger = logger;
    }
}
