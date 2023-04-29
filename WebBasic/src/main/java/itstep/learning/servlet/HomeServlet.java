package itstep.learning.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.data.DataContext;
import itstep.learning.data.entity.Task;
import itstep.learning.data.entity.Team;
import itstep.learning.data.entity.User;
import itstep.learning.model.view.TaskModel;
import itstep.learning.service.IAuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Singleton
public class HomeServlet extends HttpServlet {
    @Inject
    private DataContext dataContext;
    @Inject
    private IAuthService authService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User authUser = authService.getAuthUser();

        if (authUser != null) {
            List<Team> teams = dataContext.getTeamDao().getUserTeams( authUser.getId() );
            req.setAttribute("teams", teams );
            List<Task> tasks = dataContext.getTaskDao().getUserTasks( authUser.getId() );
            req.setAttribute("tasks", tasks );
        }

        req.setAttribute("viewName", "index");
        req.getRequestDispatcher("WEB-INF/_layout.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User authUser = authService.getAuthUser();

        try {
            if ( authUser == null )
                throw new RuntimeException( "Forbidden" );
            TaskModel taskModel = setTask( req, authUser);

            String message;
            int cnt = dataContext.getTaskDao().add(taskModel);
            if( cnt > 0 ) {
                message = "OK";
            } else {
                message = "Insert error";
            }
            resp.getWriter().print( message );
        } catch ( RuntimeException ex ) {
            resp.getWriter().print( ex.getMessage() );
        }
    }

    private TaskModel setTask(HttpServletRequest req, User authUser) throws RuntimeException {
        TaskModel taskModel = new TaskModel();
        String taskName = req.getParameter("task-name");
        if (taskName == null)
            throw new RuntimeException("Missing required parameter: task-name");
        else
            taskModel.setName(taskName);

        String taskTeam = req.getParameter("task-team");
        if (taskTeam == null)
            throw new RuntimeException("Missing required parameter: task-team");
        try {
            taskModel.setIdTeam(taskTeam);
        } catch (IllegalArgumentException ignored) {
            throw new RuntimeException("Invalid value: task-team");
        }

        String taskDeadline = req.getParameter("task-deadline");
        if (taskDeadline == null)
            throw new RuntimeException("Missing required parameter: task-deadline");
        try {
            taskModel.setDeadline(taskDeadline);
        } catch (ParseException ignored) {
            throw new RuntimeException("Invalid value: task-deadline");
        }

        String taskPriority = req.getParameter("task-priority");
        if (taskPriority == null)
            throw new RuntimeException("Missing required parameter: task-priority");
        try {
            taskModel.setPriority(taskPriority);
        } catch (NumberFormatException ignored) {
            throw new RuntimeException("Invalid value: task-priority");
        }

        taskModel.setAuthor(authUser);

        return taskModel;
    }

}
