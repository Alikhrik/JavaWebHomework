package itstep.learning.data.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.data.entity.Team;
import itstep.learning.service.IDBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TeamDao {
    private final IDBService dbService;
    private final Logger logger;

    @Inject
    public TeamDao(IDBService dbService, Logger logger) {
        this.dbService = dbService;
        this.logger = logger;
    }

    public List<Team> getAll() {
        try (Statement statement = dbService.getConnection().createStatement()) {
            ResultSet res = statement.executeQuery("SELECT t.* FROM `teams` t");
            List<Team> teamEntities = new ArrayList<>();
            while (res.next())
                teamEntities.add(new Team(res));
            return teamEntities;
        } catch (SQLException ex) {
            logger.log( Level.WARNING, ex.getMessage() );
            return null;
        }
    }

    public List<Team> getUserTeams( UUID userId ) {
        String sql = "SELECT t.id, t.name FROM `teams` t " +
                "JOIN `teams_users` tu on tu.id_team = t.id " +
                "WHERE tu.id_user = ?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString( 1, userId.toString() );
            ResultSet res = prep.executeQuery();
            List<Team> teamEntities = new ArrayList<>();
            while (res.next())
                teamEntities.add(new Team(res));
            return teamEntities;
        } catch (SQLException ex) {
            logger.log( Level.WARNING, ex.getMessage() );
            return null;
        }
    }



}
