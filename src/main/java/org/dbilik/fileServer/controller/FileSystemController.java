package org.dbilik.fileServer.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This controller gives user access to remote file system through HTTP methods
 */
@RestController
@RequestMapping(FileSystemController.CLASS_MAPPING)
public class FileSystemController extends AbstractFileController {

    protected static final String CLASS_MAPPING = "/v1/fileSystem";
    public static final String DOWNLOAD_PREFIX = "/download/";

    /*
    We can actually just autowire request. We always get object with same hashcode. And it's still thread-safe
    Spring doesn't inject new request every time an end-point is invoked
    Spring uses ThreadLocal for every request. HttpServletRequest stays the same, it just takes attributes into its threadlocal variable.
    I guess in this ? ... ServletRequestAttributes.
    So either as methoid parameter or autowired as class variable... it is practically the same.

    That also means, that we can get request anywhere in code without the need of passing it.
    Like this :
    HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    This doesn't work for HttpServletResponse
     */
    private final HttpServletRequest req;

    public FileSystemController(HttpServletRequest req) {
        this.req = req;
    }

    @GetMapping(value = DOWNLOAD_PREFIX + "**")
    public ResponseEntity<Resource> download() {
        Path path = Paths.get(parseParameterFromURI(req.getRequestURI(), DOWNLOAD_PREFIX));

        return createOkFileResponse(null);
    }
    
    /*
    download file
    upload file
    get files - get all files and its metadata in particular path/directory
    getfileTree - create whole filesystem Tree (attributes: depth, includeFileInfo - will be ignored, if tree would be too big [more than X MB])
        maybe merge getFiles and GetFileTree into 1 abstract method (more input arguments)
    delete (file or whole directory)

    create Directory
    delete Directory
    move/copy File/Directory
    getAuditInfo (about particular file/s or whole directory [globally last modification in directory/ last accessed etc...])


     */

    @Override
    protected String getClassMapping() {
        return CLASS_MAPPING;
    }
}
