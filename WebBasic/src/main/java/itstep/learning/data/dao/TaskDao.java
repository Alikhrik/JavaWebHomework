package itstep.learning.data.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.data.entity.Task;
import itstep.learning.model.view.TaskModel;
import itstep.learning.service.IDBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TaskDao {
    private final IDBService dbService;
    private final Logger logger;

    @Inject
    public TaskDao( IDBService dbService, Logger logger ) {
        this.dbService = dbService;
        this.logger = logger;
    }

    public int add( TaskModel model ) {
        String sql =
                "INSERT INTO tasks(`id`, `name`, `id_user`, `id_team`, `deadline`, `priority`)" +
                " VALUES ( UUID(), ?, ?, ?, ?, ? )";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString( 1, model.getName() );
            prep.setString( 2, model.getAuthor().getId().toString() );
            prep.setString( 3, model.getIdTeam().toString() );
            prep.setString( 4, model.formFormat.format( model.getDeadline() ) );
            prep.setInt(5, model.getPriority() );
            return prep.executeUpdate();
        } catch ( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() );
        }
        return 0;
    }

    public List<Task> getUserTasks(UUID userId ) {
        String sql = "SELECT t.* FROM `tasks` t JOIN `teams_users` tu on t.id_team = tu.id_team WHERE tu.id_user = ?";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement( sql ) ) {
            prep.setString( 1, userId.toString() );
            ResultSet res = prep.executeQuery();
            List<Task> taskEntities = new ArrayList<>();
            while ( res.next() )
                taskEntities.add( new Task( res ) );
            return taskEntities;
        } catch (SQLException ex) {
            logger.log( Level.WARNING, ex.getMessage() );
        }
        return null;
    }
    public Task getTaskById( UUID taskId ) {
        String sql = "SELECT * FROM `tasks`  WHERE id = ?";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement( sql ) ) {
            prep.setString( 1, taskId.toString() );
            ResultSet res = prep.executeQuery();

            if ( res.next() ) {
                Task task = new Task( res );
                res.close();
                return task;
            }

        } catch (SQLException ex) {
            logger.log( Level.WARNING, ex.getMessage() );
        }
        return null;
    }

}
