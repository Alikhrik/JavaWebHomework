package itstep.learning.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.model.FormsModel;
import itstep.learning.service.IHashService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Singleton
public class FormsServlet extends HttpServlet {
    @Inject
    private IHashService hashService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        req.setAttribute("viewName", "forms");
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormsModel model = FormsModel.parse(req);
        model.setMethod(req.getMethod());
        HttpSession session = req.getSession();
        model.setText(model.getText() + " " + hashService.getHexHash(model.getText()));

        session.setAttribute("model", model);
        resp.sendRedirect(req.getRequestURI());
    }
}
