package itstep.learning.model.view;

import itstep.learning.data.entity.Story;
import itstep.learning.data.entity.User;

import java.util.UUID;

public class StoryModel {
    private User author;
    private UUID taskId;  // TaskEntity
    private UUID replyId; // StoryEntity
    private String content;

    // region Attributes
    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public void setReplyId(UUID replyId) {
        this.replyId = replyId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public UUID getReplyId() {
        return replyId;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // endregion

    public Story toStoryEntity() {
        Story entity = new Story();
        entity.setAuthorId( getAuthor().getId() );
        entity.setTaskId( getTaskId() );
        entity.setReplyId( getReplyId() );
        entity.setContent( getContent() );
        return entity;
    }
}
