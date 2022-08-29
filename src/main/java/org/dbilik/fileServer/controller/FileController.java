package org.dbilik.fileServer.controller;

import org.dbilik.fileServer.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(FileController.CLASS_MAPPING)
public class FileController extends AbstractFileController {

    protected static final String CLASS_MAPPING = "/v1/file";

    private final DownloadService downloadService;

    @Autowired
    public FileController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @GetMapping(value = "/download/{uuid}")
    public void download(@PathVariable("uuid") String uuid, final HttpServletResponse response) {
        log.debug("FileController.download called with param[uuid={}]", uuid);
        downloadService.download(uuid, response);
    }


    @Override
    protected String getClassMapping() {
        return CLASS_MAPPING;
    }
}
