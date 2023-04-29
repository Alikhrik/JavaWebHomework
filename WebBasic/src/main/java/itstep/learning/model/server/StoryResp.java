package itstep.learning.model.server;

import itstep.learning.data.DataContext;
import itstep.learning.data.entity.Story;
import itstep.learning.data.entity.Task;
import itstep.learning.data.entity.User;

import java.util.UUID;

public class StoryResp {
    private UUID id;
    private String author;
    private TaskResp task;
    private String content;
    private StoryResp reply;
    private String createdDate;

    public StoryResp(UUID id, String author, TaskResp task, String content, StoryResp reply, String createdDate) {
        this.id = id;
        this.author = author;
        this.task = task;
        this.content = content;
        this.reply = reply;
        this.createdDate = createdDate;
    }


    public static StoryResp parse( Story entity, DataContext dataContext ) {
        UUID id = entity.getId();
        User author = dataContext.getUserDao().getUserById( entity.getAuthorId() );
        Task taskEntity = dataContext.getTaskDao().getTaskById( entity.getTaskId() );
        TaskResp taskResp = new TaskResp( taskEntity );

        String content = entity.getContent();
        StoryResp reply = entity.getReplyId() != null
                ? parse( dataContext.getStoryDao().getStoryById( entity.getReplyId() ), dataContext )
                : null ;
        String createdDate = entity.getCreatedDate().toOffsetDateTime().toString();
        return new StoryResp(
                id,
                author.getName(),
                taskResp,
                content,
                reply,
                createdDate );
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public TaskResp getTask() {
        return task;
    }

    public void setTask(TaskResp task) {
        this.task = task;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StoryResp getReply() {
        return reply;
    }

    public void setReply(StoryResp reply) {
        this.reply = reply;
    }
}
