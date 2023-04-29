package itstep.learning.service;

import java.sql.Connection;

public interface IDBService {
    Connection getConnection() throws RuntimeException;
}
