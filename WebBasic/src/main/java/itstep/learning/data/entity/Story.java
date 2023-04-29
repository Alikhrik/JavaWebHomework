package itstep.learning.data.entity;

import java.sql.ResultSet;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Story extends Entity {
    private UUID id;
    private UUID authorId;
    private UUID taskId;
    private UUID replyId; // StoryEntity
    private String content;
    private ZonedDateTime createdDate;
    public Story() {

    }
    public Story(ResultSet resultSet) throws RuntimeException {
        try {
            setId(         UUID.fromString( resultSet.getString(1) ) );
            setAuthorId(    UUID.fromString( resultSet.getString(2) ) );
            setTaskId(    UUID.fromString( resultSet.getString(3) ) );
            String replyId = resultSet.getString(4);
            if( !replyId.equals("null")) {
                setReplyId( UUID.fromString( replyId ) );
            }
            setContent(    resultSet.getString(5) );

            ZonedDateTime createdDt = ZonedDateTime.of(
                    resultSet.getTimestamp(6).toLocalDateTime(),
                    ZoneId.of("Etc/UTC")
            );
            setCreatedDate( createdDt );
        } catch (Exception ex) {
            throw new RuntimeException( ex.getMessage() );
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public UUID getReplyId() {
        return replyId;
    }

    public String getContent() {
        return content;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public void setReplyId(UUID replyId) {
        this.replyId = replyId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

//    public void setCreatedDate( String createdDate ) throws ParseException {
//        this.createdDate = formFormat.parse( createdDate );
//    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }
    public String getCreatedDateString() {
        return formFormat.format( createdDate );
    }
}
