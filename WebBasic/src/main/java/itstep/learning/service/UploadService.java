package itstep.learning.service;

import java.io.File ;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;

public class UploadService {
    private final DiskFileItemFactory fileItemFactory ;
    private final ServletFileUpload fileUpload ;
    @Inject
    private IHashService hashService;

    public UploadService() {
        fileItemFactory = new DiskFileItemFactory() ;
        fileItemFactory.setSizeThreshold( 20480 ) ;  // размер файла в памяти. Если больше - на диск
        File tmpDir = new File( "C:/tmp" ) ;
        if( ! tmpDir.exists() ) {
            if( ! tmpDir.mkdir() ) {
                tmpDir = null ;
            }
        }
        if( tmpDir != null ) {
            fileItemFactory.setRepository( tmpDir ) ;
        }
        // FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker( servletContext ) ;
        fileUpload = new ServletFileUpload( fileItemFactory ) ;
        // upload.setFileSizeMax(MAX_FILE_SIZE);
        // upload.setSizeMax(MAX_REQUEST_SIZE);
    }

    public Map<String, FileItem> parse( HttpServletRequest request ) throws FileUploadException {
        return fileUpload
                .parseRequest( request )
                .stream()
                .collect(
                        Collectors.toMap(
                                FileItem::getFieldName,
                                Function.identity() ) ) ;
    }

    public String saveAvatar(FileItem avatar, String path) throws Exception {
        if ( avatar.isFormField()) {    // это файловое поле
            return null;
        }
        if (avatar.getSize() < 0) {  // нема даних
            return null;
        }
        String filename = avatar.getName();
        int dotPosition = filename.lastIndexOf('.');
        String extension = filename.substring(dotPosition);
        // TODO: проверить на допустимое расширение файла
        switch (extension) {
            case ".jpg":
            case ".gif":
            case ".png":
                break;
            default:
                throw new IllegalArgumentException("Extension " + extension + " is not supported");
        }
        // Имя файла сохранять опасно - генерируем случайное имя и проверяем на существование файла
        String savedName = "";
        File file;
        do {
            savedName = hashService.getHexHash(savedName + System.nanoTime()) + extension;
            file = new File(path, savedName);
        } while (file.exists());
        avatar.write(file);  // сохраняем файл в постоянном хранилище
        return savedName;
    }
}