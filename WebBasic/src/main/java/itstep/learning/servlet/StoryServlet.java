package itstep.learning.servlet;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.data.DataContext;
import itstep.learning.data.entity.Story;
import itstep.learning.data.entity.User;
import itstep.learning.model.server.StoryResp;
import itstep.learning.model.view.StoryModel;
import itstep.learning.service.IAuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class StoryServlet extends HttpServlet {
    @Inject
    private DataContext dataContext;
    @Inject
    private IAuthService authService;
    private static final Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String taskId = req.getParameter("taskId");
        List<Story> stories = null;
        try {
            stories = dataContext.getStoryDao().getTaskStories( UUID.fromString( taskId ) );
        } catch ( IllegalArgumentException ignored ) {
            resp.getWriter().print( "{ \"status\": 400," +
                    " \"message\": \"Invalid parameter: taskId (UUID-required)\" }" );
            return;
        }

        if( stories != null && stories.size() > 0 ) {
            List<StoryResp> chatMessages = new ArrayList<>();
            for ( Story story : stories ) {
                chatMessages.add( StoryResp.parse( story , dataContext ) );
            }
            resp.getWriter().print( gson.toJson( chatMessages ) );
        } else {
            resp.getWriter().print( "{ \"status\": 400," +
                    " \"message\": \"Task has not stories\" }" );
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User authUser = authService.getAuthUser();

        try {
            if ( authUser == null )
                throw new RuntimeException( "Forbidden" );
            StoryModel storyModel = setStory( req, authUser);

            String message;
            int cnt = dataContext.getStoryDao().add(storyModel);
            if( cnt > 0 ) {
                message = "OK";
            } else {
                message = "Insert error";
            }
            resp.getWriter().print( message );
        } catch (RuntimeException ex) {
            resp.getWriter().print( ex.getMessage() );
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    private StoryModel setStory(HttpServletRequest req, User authUser ) throws RuntimeException {
        StoryModel storyModel = new StoryModel();
        storyModel.setAuthor( authUser );

        String taskId = req.getParameter("story-id-task");
        if ( taskId == null || "".equals( taskId.trim() ) )
            throw new RuntimeException("Missing required parameter: story-id-task");
        try {
            storyModel.setTaskId( UUID.fromString( taskId ) );
        } catch (IllegalArgumentException ignored) {
            throw new RuntimeException("Invalid value: story-id-task");
        }

        String replyId = req.getParameter("story-id-reply");
        if ( replyId != null && !"".equals( replyId.trim() ) ) {
            try {
                storyModel.setReplyId( UUID.fromString( replyId ) );
            } catch (IllegalArgumentException ignored) {
                throw new RuntimeException("Invalid value: story-id-reply");
            }
        }

        String content = req.getParameter("story-text");
        if ( content == null || "".equals( content.trim() ) )
            throw new RuntimeException("Missing required parameter: story-text");
        try {
            storyModel.setContent( content );
        } catch (IllegalArgumentException ignored) {
            throw new RuntimeException("Invalid value: story-text");
        }
        return storyModel;
    }
}
