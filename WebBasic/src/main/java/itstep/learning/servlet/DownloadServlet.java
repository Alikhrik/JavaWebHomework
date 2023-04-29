package itstep.learning.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

@Singleton
public class DownloadServlet extends HttpServlet {
    @Inject @Named("AvatarFolder")
    private String avatarFolder;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletContext().getRealPath("/") + avatarFolder;
        File file = new File( path, req.getPathInfo() );

        if( file.isFile() && file.canRead() ) {
            String[] warning_types = {
                    ".class",
                    ".php",
                    ".exe"
            };
            for (String type: warning_types) {
                if (file.getName().endsWith(type)) {
                    resp.setStatus(415);
                    resp.getWriter().println( "Unsupported Media Type " );
                    System.err.println( "Dangerous type: " + type );
                    return;
                }
            }

            String mimeType = Files.probeContentType( file.toPath() );
            resp.setContentType( mimeType );
            if( !mimeType.startsWith("image/") ) {
                resp.setStatus(415);
                resp.getWriter().println( "Unsupported Media Type: " + mimeType );
                System.err.println( "Unsupported Media Type: " + mimeType );
                return;
            }

            switch ( mimeType ) {
                case "image/gif":
                case "image/avif":
                    resp.setStatus(415);
                    resp.getWriter().println( "Unsupported Media Type: " + mimeType );
                    System.err.println( "Unsupported media type: " + mimeType );
                    return;
            }

            byte[] buf = new byte[1024];
            try( InputStream reader = Files.newInputStream(file.toPath());
                 OutputStream writer = resp.getOutputStream() ) {
                int len;
                while( (len = reader.read( buf ) ) != -1 ) {
                    writer.write( buf, 0, len );
                }
            } catch ( IOException ex ) {
                resp.setStatus(500);
                System.err.println("DownloadServlet: " + ex.getMessage());
            }
        }
    }
}
