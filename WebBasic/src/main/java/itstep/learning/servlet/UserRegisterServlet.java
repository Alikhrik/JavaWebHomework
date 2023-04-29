package itstep.learning.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import itstep.learning.data.dao.UserDao;
import itstep.learning.model.view.UserModel;
import itstep.learning.service.IHashService;
import itstep.learning.service.UploadService;
import org.apache.commons.fileupload.FileItem;
import org.json.JSONObject;
//import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class UserRegisterServlet extends HttpServlet {

    @Inject
    private UploadService uploadService;
    @Inject
    private IHashService hashService;
    @Inject
    private UserDao userDao;
    @Inject @Named("AvatarFolder")
    private String avatarFolder;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("viewName", "UserRegistration");
        HttpSession session = req.getSession();
        String regMessage = (String) session.getAttribute("reg-message");
        String regErrors = (String) session.getAttribute("reg-errors");
        if (regMessage != null) {
            req.setAttribute("reg-message", regMessage);
            session.removeAttribute("reg-message");
        }
        if(regErrors != null) {
            req.setAttribute("reg-errors", regErrors);
            session.removeAttribute("reg-errors");
        }
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String regMessage = null;
        String regErrors = null;
        try {
            UserModel model = parseModel(req);
            validateModel(model);
            if (userDao.add(model)) {
                regMessage = "OK";
            } else {
                regMessage = "Add fail";
            }
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            regErrors = ex.getMessage();
        }
        HttpSession session = req.getSession();
        session.setAttribute("reg-message", regMessage);
        session.setAttribute("reg-errors", regErrors);
        resp.sendRedirect(req.getRequestURI());
    }

    private UserModel parseModel(HttpServletRequest req) throws IllegalArgumentException {
        try {
            Map<String, FileItem> parameters = uploadService.parse(req);
            UserModel model = new UserModel();
            model.setLogin(parameters.get("user-login").getString());
            model.setPass1(parameters.get("user-pass1").getString());
            model.setPass2(parameters.get("user-pass2").getString());
            model.setName(parameters.get("user-name").getString());
            model.setEmail(parameters.get("user-email").getString());
            // проверка и сохранение файла
            FileItem avatar = parameters.get("user-avatar");
            String path = req.getServletContext().getRealPath("/") + avatarFolder;
            String savedName = uploadService.saveAvatar( avatar, path );
            model.setAvatar(savedName);
            return model;
        } catch (Exception ex) {
            if (ex instanceof IllegalArgumentException) throw (IllegalArgumentException) ex;
            System.err.println(ex.getMessage());
            return null;
        }
    }

    private void validateModel( UserModel model ) throws IllegalArgumentException {
        Map<String, String> errors = new HashMap<>();

        if( model != null ) {
            // login validate
            if( model.getLogin() == null )
                errors.put("user-login", "Missing parameter: user-login");
            else {
                String login = model.getLogin() ;
                if( "".equals( login ) )
                    errors.put("user-login", "user-login should not be empty");
                else if (!userDao.IsFreeLogin(login)) {
                    // TODO: проверить логин на занятость
                    errors.put("user-login", "user-login is used");
                } else if (login.length() < 4) {
                    errors.put("user-login", "user-login must contain more than 4 symbols");
                }
            }

            // pass validate
            String pass1 = model.getPass1() ;
            String pass2 = model.getPass2() ;
            if( pass1 == null || pass2 == null )  {
                errors.put("user-pass", "Password too short");
            }
            else {
                if( pass1.length() < 3 )
                    errors.put("user-pass", "Password too short");
                if( ! pass1.equals( pass2 ) )
                    errors.put("user-pass", "Passwords mismatch");
                // TODO: проверить пароль на сложность
            }
        }
        else {
            errors.put("miss-all-par", "Missing all parameters");
        }

        String JSONString = new JSONObject(errors).toString();
        if( !errors.isEmpty() ) {
            throw new IllegalArgumentException( JSONString ) ;
        }
    }
}