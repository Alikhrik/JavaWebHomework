package itstep.learning.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import itstep.learning.data.dao.UserDao;
import itstep.learning.data.entity.User;
import itstep.learning.service.IAuthService;
import itstep.learning.service.IHashService;
import itstep.learning.service.UploadService;
import org.apache.commons.fileupload.FileItem;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Singleton
public class UserProfileServlet extends HttpServlet {
    @Inject
    private UploadService uploadService;
    @Inject
    private UserDao userDao;
    @Inject
    private IHashService hashService;
    @Inject
    private Logger logger;
    @Inject
    private IAuthService authService;

    @Inject @Named("AvatarFolder")
    private String avatarFolder;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String profileUserLogin = req.getPathInfo(); // /%user%
        User auth_user = authService.getAuthUser();
        if( profileUserLogin != null && profileUserLogin.length() > 1 ) {
            profileUserLogin = profileUserLogin.substring(1);

            User profile = userDao.getUserWithoutCredentials(profileUserLogin);
            if ( profile != null ) { // is auth profile
                if( auth_user != null && auth_user.getId().equals(profile.getId())) {
                    req.setAttribute("viewName", "auth-user-profile");
                    req.setAttribute("profile", auth_user);
                } else { // is not auth profile
                    req.setAttribute("viewName", "user-profile");
                    req.setAttribute("profile", profile );
                }
            } else { // profile does not exist
                resp.setStatus(404);
                resp.getWriter().println("Profile \"" + profileUserLogin + "\" does not exist!");
                return;
            }

        } else {
            if( auth_user != null ) {
                req.setAttribute("viewName", "auth-user-profile");
                req.setAttribute("profile", auth_user);
            } else {
                resp.setStatus(404);
                return;
            }
        }

        req.getRequestDispatcher("../WEB-INF/_layout.jsp").forward( req, resp );
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String param = req.getParameter("update");
        User auth_user = authService.getAuthUser();
        if( auth_user == null ) {
            resp.setStatus(403);
            resp.getWriter().println("Forbidden");
            return;
        }

        String body;
        JSONObject body_obj = null;
        if( req.getHeader("Content-Type").equals("application/json") ) {
            try( InputStream bodyStream = req.getInputStream() ) {
                byte[] buf = new byte[1024];
                ByteArrayOutputStream arr = new ByteArrayOutputStream() ;
                int len;
                while( ( len = bodyStream.read( buf ) ) != -1 ) {
                    arr.write( buf, 0, len );
                }
                body = arr.toString("UTF-8" );
                body_obj = new JSONObject( body );
                arr.close();
            } catch ( Exception ex ) {
                logger.log( Level.SEVERE, ex.getMessage() ) ;
                resp.getWriter().print("error");
                return;
            }
        }

        if( param.equals("email") && body_obj.has("user-email") ) {
            String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            String email = body_obj.optString("user-email" );
            if( email == auth_user.getName() ) {
                resp.getWriter().print("NO");
                return;
            }

            if( !Pattern.matches(regex, email) ) {
                resp.getWriter().print("NO");
                return;
            }

            int cnt = userDao.setUserEmail( auth_user.getId(), email );

            if( cnt > 0 ) {
                auth_user.setEmail( email );
                resp.getWriter().print("OK");
            } else {
                resp.getWriter().print("error");
            }
        }
        else if( param.equals("name") && body_obj.has("user-name") ) {
            String name = body_obj.optString("user-name");
            if(name.equals(auth_user.getName())) {
                resp.getWriter().print("NO");
                return;
            }
            if( name.length() < 4 ) {
                resp.getWriter().print("NO");
                return;
            }

            int cnt = userDao.setUserName( auth_user.getId(), name );

            if( cnt > 0 ) {
                auth_user.setName( name );
                resp.getWriter().print("OK");
            } else {
                resp.getWriter().print("error");
            }
        }
        else if( param.equals("pass") && body_obj.has("old-pass") &&
                 body_obj.has("new-pass") && body_obj.has("rep-pass") ) {

            String old_pass = body_obj.optString("old-pass");
            String new_pass = body_obj.optString("new-pass");
            String rep_pass = body_obj.optString("rep-pass");

            String salt = auth_user.getSalt();
            String old_pass_hash = hashService.getHexHash(salt + old_pass );
            String new_pass_hash = hashService.getHexHash(salt + new_pass );

            if( old_pass.equals("") ) {
                resp.getWriter().print("Old password is empty!"); return;
            } else if ( new_pass.equals("") ) {
                resp.getWriter().print("New password is empty!"); return;
            } else if ( rep_pass.equals("") ) {
                resp.getWriter().print("Repeated password is empty!!"); return;
            }

            if( !new_pass.equals(rep_pass) ) {
                resp.getWriter().print("Passwords mismatch!"); return;
            }

            if( new_pass.trim().length() < 4 ) {
                resp.getWriter().print("New password is too short!"); return;
            }

            if( !old_pass_hash.equals(auth_user.getPass()) ) {
                resp.getWriter().print("The old password is incorrect!"); return;
            }

            if( new_pass_hash.equals(old_pass_hash) ) {
                resp.getWriter().print("The new password must be different from the old one!"); return;
            }

            int cnt = userDao.setUserPass(auth_user.getId(), new_pass );
            if( cnt > 0 ) {
                User newAuthUser = userDao.getUserByCredentials( auth_user.getLogin(), new_pass );
                if( auth_user != null ) {
                    auth_user.setPass( newAuthUser.getPass() );
                    auth_user.setSalt( newAuthUser.getSalt() );
                    resp.getWriter().print("OK"); return;
                }
            }

            resp.getWriter().print("error"); return; // end
        }
        else if( param.equals("avatar") && req.getHeader("Content-Type").startsWith("multipart/form-data") ) {
            try {
                Map<String, FileItem> params = uploadService.parse(req);
                FileItem avatar = params.get("user-avatar");
                String path = req.getServletContext().getRealPath("/") + avatarFolder;
                String savedName = uploadService.saveAvatar( avatar, path );
                int cnt = userDao.setUserAvatar(auth_user.getId(), savedName);
                if(cnt > 0) {
                    File old_avatar = new File( path, auth_user.getAvatar() );
                    if( old_avatar.delete() ) {
                        auth_user.setAvatar(savedName);
                        resp.getWriter().print("OK");
                    } else {
                        logger.log( Level.WARNING, "Failed to delete the file: " + auth_user.getAvatar() );
                        resp.getWriter().print("error");
                        return;
                    }
                } else {
                    logger.log( Level.WARNING, "Avatar not uploaded: " + auth_user.getAvatar() );
                    resp.getWriter().print("error");
                    return;
                }
            } catch (Exception ex) {
                logger.log( Level.WARNING, ex.getMessage() );
                resp.getWriter().print("error");
                return;
            }
        }
    }
}
