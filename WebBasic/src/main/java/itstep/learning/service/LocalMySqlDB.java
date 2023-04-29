package itstep.learning.service;

import com.google.inject.Singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Singleton
public class LocalMySqlDB implements IDBService {
    private Connection connection ;
    @Override
    public Connection getConnection() throws RuntimeException {
        if( connection == null ) {
            try {
                 Class.forName( "com.mysql.cj.jdbc.Driver" ) ;
//                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                connect() ;
            } catch(SQLException ex ) {
                throw new RuntimeException( "SQLException: " +  ex.getMessage() ) ;
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException( "SQLException: " +  ex.getMessage() );
            }
        }
        else {  // НО! если БД отключится после предварительного успешного подключения,
            // то проверки на null недостаточно, нужно выполнять пробный запрос
            try( PreparedStatement prep =
                         connection.prepareStatement( "SELECT 1" ) ) {
                prep.execute() ;
            }
            catch( Exception ignored ) {   // пробный запрос не удался
                try { connect() ; }        // пробуем переподключиться
                catch( Exception ex ) {    // если не удалось - исключение
                    throw new RuntimeException( ex.getMessage() ) ;
                }
            }
        }
        return connection ;
    }



    private void connect() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/java_011?useUnicode=true&characterEncoding=UTF-8",
                "user_011", "pass_011"
        ) ;
    }
}
