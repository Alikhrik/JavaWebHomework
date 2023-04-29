package itstep.learning.ws;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Inject;
import itstep.learning.data.DataContext;
import itstep.learning.data.entity.Story;
import itstep.learning.data.entity.User;
import itstep.learning.model.server.StoryResp;
import itstep.learning.model.view.StoryModel;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


@ServerEndpoint(
        value = "/chat",
        configurator = WebsocketConfigurator.class ) // займеється створенням екземплярів класу
public class WebsocketServer {
    private final DataContext dataContext;
    private final Logger logger;
    private static final Set<Session> sessions =
            Collections.synchronizedSet(
                    new HashSet<>()
            );
    private static final Gson gson = new Gson();
    @Inject
    public WebsocketServer(DataContext dataContext, Logger logger) {
        this.dataContext = dataContext;
        this.logger = logger;
    }

    @OnOpen
    public void onOpen( Session session, EndpointConfig configurator ) {
        User authUser = (User) configurator.getUserProperties().get( "authUser" );
        if( authUser == null ) {
            onClose( session );
            return;
        }
        session.getUserProperties()
                .put( "authUser", authUser);
        sessions.add( session );
    }

    @OnMessage
    public void onMessage( String message, Session session ) {
        Story story = addStory( message, session );
        if( story == null ) {
            try {
                session.getBasicRemote().sendText( "{ \"status\": 400 }" );
            } catch ( IOException ex ) {
                ex.printStackTrace();
            }
        } else {
            StoryResp result = StoryResp.parse( story , dataContext );
            sendToAll( gson.toJson( result ));
        }
    }

    @OnClose
    public void onClose( Session session ) {
        User authUser = (User) session.getUserProperties().get( "authUser" );
        sessions.remove( session );
//        if ( authUser != null ) {
//            String message = String.format( "{ 'id': '%s', 'content': 'Exit chat' }",
//                    authUser.getName()
//            );
//            sendToAll( message.replace('\'', '"') );
//        }
    }

    @OnError
    public void onError( Throwable ex, Session session ) {
        ex.printStackTrace();
    }

    private void sendToAll( String message ) {
        for( Session session : sessions ) {
            try {
                session.getBasicRemote().sendText( message );
            } catch ( IOException ex ) {
                ex.printStackTrace();
            }
        }
    }

    private Story addStory(String message, Session session ) {
        ChatMessage chatMessage;
        try {
            chatMessage = gson.fromJson( message, ChatMessage.class );
        } catch ( JsonSyntaxException ex ) {
            logger.log( Level.WARNING, ex.getMessage() );
            return null;
        }

        User authUser = (User) session.getUserProperties().get( "authUser" );
        String content = chatMessage.getContent();
        UUID taskId = chatMessage.getTaskId();

        StoryModel model = new StoryModel();
        if( content != null && !content.trim().equals("") ) {
            model.setContent( chatMessage.getContent() );
        } else return null;

        if( taskId != null ) {
            model.setTaskId( taskId );
        } else return null;
        model.setAuthor(authUser);

        Story entity = dataContext.getStoryDao().create( model );
        if( entity == null ) {
            logger.log( Level.WARNING, "Insert error" );
        }
        return entity;
    }

    public static class ChatMessage {
        private String author;
        private UUID taskId;
        private String content;
        private UUID replyStoryId;
        public ChatMessage() {}

        public UUID getReplyStoryId() {
            return replyStoryId;
        }

        public void setReplyStoryId(UUID replyStoryId) {
            this.replyStoryId = replyStoryId;
        }

        public UUID getTaskId() {
            return taskId;
        }

        public void setTaskId(UUID taskId) {
            this.taskId = taskId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }
}
