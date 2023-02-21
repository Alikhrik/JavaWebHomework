package itstep.learning.servlet;

import itstep.learning.model.FormsModel;
import javafx.scene.paint.Color;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/forms")
public class FormsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String isFromForm = req.getParameter("isFromForm");
        if(isFromForm != null && isFromForm.equals("true")) {
            FormsModel model = FormsModel.parse(req);
            model.setMethod(req.getMethod());
            model.setMessage(req.getParameter("isFromForm"));
            req.setAttribute("data", model);
        }

        req.getRequestDispatcher("WEB-INF/forms.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormsModel model = FormsModel.parse(req);
        model.setMethod(req.getMethod());
        req.setAttribute("data", model);

        req.getRequestDispatcher("WEB-INF/forms.jsp")
                .forward(req, resp);
    }
}
