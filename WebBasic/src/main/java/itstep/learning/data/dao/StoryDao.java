package itstep.learning.data.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.data.entity.Story;
import itstep.learning.model.view.StoryModel;
import itstep.learning.service.IDBService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class StoryDao extends Dao {
    @Inject
    public StoryDao(IDBService dbService, Logger logger) {
        super(dbService, logger);
    }

    public int add( StoryModel model ) {
        String sql =
                "INSERT INTO stories( `id`, `id_user`, `id_task`, `id_reply`, `content` )" +
                " VALUES ( UUID(), ?, ?, ?, ? )";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString( 1, model.getAuthor().getId().toString() );
            prep.setString( 2, model.getTaskId().toString() );
            prep.setString( 3, model.getReplyId() == null ? "null" :
                                            model.getReplyId().toString() );
            prep.setString( 4, model.getContent() );
            return prep.executeUpdate();
        } catch ( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() );
        }
        return 0;
    }
    public Story create( StoryModel model ) {
        Story entity = model.toStoryEntity();
        entity.setId( UUID.randomUUID() );
        entity.setCreatedDate( ZonedDateTime.now( ZoneId.of( "Etc/UTC" ) ) );

        String sql =
                "INSERT INTO stories( `id`, `id_user`, `id_task`, `id_reply`, `content`, `created_dt` )" +
                        " VALUES ( ?, ?, ?, ?, ?, ? )";
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString( 1, entity.getId().toString() );
            prep.setString( 2, entity.getAuthorId().toString() );
            prep.setString( 3, entity.getTaskId().toString() );
            prep.setString( 4, entity.getReplyId() == null ? "null" :
                    entity.getReplyId().toString() );
            prep.setString( 5, entity.getContent() );
            prep.setTimestamp(6, Timestamp.valueOf( entity.getCreatedDate().toLocalDateTime() ) );

            int cnt = prep.executeUpdate();
            if( cnt != 0 ) {
                return entity;
            } else {
                throw new RuntimeException( "Insert error" );
            }
        } catch ( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() );
        }
        return null;
    }

    public List<Story> getTaskStories( UUID taskId ) {
        String sql = "SELECT * FROM `stories` WHERE id_task = ?";// ORDER BY `created_dt`
        try( PreparedStatement prep = dbService.getConnection().prepareStatement(sql) ) {
            prep.setString( 1, taskId.toString() );
            ResultSet res = prep.executeQuery();
            List<Story> stories = new ArrayList<>();
            while ( res.next() ) {
                stories.add( new Story( res ) );
            }
            return stories;
        } catch ( SQLException ex ) {
            logger.log( Level.WARNING, ex.getMessage() );
        }
        return null;
    }
    public Story getStoryById( UUID storyId ) {
        String sql = "SELECT * FROM `stories` WHERE id = ?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, storyId.toString() );
            ResultSet res = prep.executeQuery();

            if (res.next()) {
                Story result = new Story(res);
                res.close();
                return result;
            }

        } catch (SQLException ex) {
            logger.log( Level.WARNING, "SELECT Exception: " + ex.getMessage() );
        }
        return null;
    }
}
