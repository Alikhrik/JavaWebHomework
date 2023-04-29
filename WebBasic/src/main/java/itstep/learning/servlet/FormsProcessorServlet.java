package itstep.learning.servlet;

import com.google.inject.Singleton;
import itstep.learning.model.FormsModel;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

@Singleton
public class FormsProcessorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("viewName", "formsProcessor");
        HttpSession session = req.getSession();
        FormsModel model = (FormsModel) session.getAttribute("model");
        if(model == null) {
            String isFromForm = req.getParameter("isFromForm");
            if(isFromForm != null && isFromForm.equals("true")) {
                model = FormsModel.parse(req);
                model.setMethod(req.getMethod());
                req.setAttribute("model", model);
            }
        } else {
            session.removeAttribute("model");
            req.setAttribute("model", model);
        }

        req.getRequestDispatcher("WEB-INF/_layout.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
