package itstep.learning.data.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.data.entity.User;
import itstep.learning.model.view.UserModel;
import itstep.learning.service.IDBService;
import itstep.learning.service.IHashService;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class UserDao {   // Data Access Object  for entity.User
    private final IDBService dbService;
    private final Logger logger;
    private final IHashService hashService;

    @Inject
    public UserDao(IDBService dbService, IHashService hashService, Logger logger) {
        this.dbService = dbService;
        this.hashService = hashService;
        this.logger = logger;
    }

    public List<User> getAll() {
        try (Statement statement = dbService.getConnection().createStatement()) {
            ResultSet res = statement.executeQuery("SELECT u.* FROM Users u");
            List<User> userEntities = new ArrayList<>();
            while (res.next())
                userEntities.add(new User(res));
            return userEntities;
        } catch (SQLException ex) {
            System.err.println("UserDao::getAll " + ex.getMessage());
            return null;
        }
    }

    public boolean add(@Nonnull UserModel model) {
        // Генерируем соль
        String salt = hashService.getHexHash(System.nanoTime() + "");
        // Создаем хеш пароля
        String hash = getPassHash(model.getPass1(), salt);
        String sql = "INSERT INTO Users(`id`,`login`,`name`,`salt`,`pass`,`email`, `avatar`) " +
                " VALUES( UUID(), ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, model.getLogin());
            prep.setString(2, model.getName());
            prep.setString(3, salt);
            prep.setString(4, hash);
            prep.setString(5, model.getEmail());
            prep.setString(6, model.getAvatar());
            prep.executeUpdate();
            return true;
        } catch (Exception ex) {
            System.err.println("UserDao::add " + ex.getMessage());
            return false;
        }
    }

    public boolean IsFreeLogin(String login) {
        String sql = "SELECT COUNT(login) FROM users WHERE login = ?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, login);
            ResultSet resultSet = prep.executeQuery();
            if (resultSet.next()) {
                int cnt = resultSet.getInt(1);
                if (cnt != 0) return false;
            }
            return true;

        } catch (SQLException ex) {
            throw new RuntimeException("SELECT Exception: " + ex.getMessage());
        }
    }

    public User getUserByCredentials(String login, String pass) {
        User user = getUserByLogin( login );
        if(user != null) {
            String hash = getPassHash( pass, user.getSalt() );
            if (!hash.equals( user.getPass()) )
                return null;
            else
                return user;
        } else {
            return null;
        }
    }

    private User getUserByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, login);
            ResultSet res = prep.executeQuery();

            if (res.next()) {
                User result = new User(res);
                res.close();
                return result;
            }

        } catch (SQLException ex) {
            throw new RuntimeException( "SELECT Exception: " + ex.getMessage() );
        }
        return null;
    }

    public User getUserWithoutCredentials(String login) {
        User user = getUserByLogin(login);
        if( user != null ) {
            user.setPass( null );
            user.setSalt( null );
            user.setConfirm( null );
            return user;
        } else {
            return null;
        }
    }

    public int setUserName( UUID id, String name ) { // returns the numbers of updated rows
        String sql = "UPDATE users as u SET u.name = ? WHERE u.id = ?";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, name );
            prep.setString( 2, id.toString() );
            return prep.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "UPDATE Exception: " + ex.getMessage());
        }
        return 0;
    }

    public int setUserEmail( UUID id, String email ) { // returns the numbers of updated rows
        String sql = "UPDATE users as u SET u.email = ? WHERE u.id = ?";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, email );
            prep.setString( 2, id.toString() );
            return prep.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "UPDATE Exception: " + ex.getMessage());
        }
        return 0;
    }

    public int setUserAvatar( UUID id, String avatar_name ) { // returns the numbers of updated rows
        String sql = "UPDATE users as u SET u.avatar = ? WHERE u.id = ?";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, avatar_name );
            prep.setString( 2, id.toString() );
            return prep.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "UPDATE Exception: " + ex.getMessage());
        }
        return 0;
    }

    public int setUserPass( UUID id, String pass ) { // returns the numbers of updated rows
        String sql = "UPDATE users as u SET u.salt = ?, u.pass = ? WHERE u.id = ?";

        String salt = hashService.getHexHash(System.nanoTime() + "");
        String hash = getPassHash( pass , salt );

        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, salt );
            prep.setString(2, hash );
            prep.setString( 3, id.toString() );
            return prep.executeUpdate();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "UPDATE Exception: " + ex.getMessage());
        }
        return 0;
    }
    public User getUserById( UUID userId ) {
        String sql = "SELECT * FROM `users` WHERE id = ?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, userId.toString() );
            ResultSet res = prep.executeQuery();

            if (res.next()) {
                User result = new User(res);
                res.close();
                return result;
            }

        } catch (SQLException ex) {
            logger.log( Level.WARNING, "SELECT Exception: " + ex.getMessage() );
        }
        return null;
    }

    private String getPassHash(String password, String salt) {
        return hashService.getHexHash(salt + password);
    }
}