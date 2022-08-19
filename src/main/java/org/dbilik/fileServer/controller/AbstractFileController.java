package org.dbilik.fileServer.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public abstract class AbstractFileController extends AbstractController {

    protected String parseParameterFromURI(String uri, String methodPath) {
        return uri.substring((getClassMapping() + methodPath).length());
    }

    protected abstract String getClassMapping();

    protected ResponseEntity<Resource> createOkFileResponse(Resource resource) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "Content-Disposition", "attachment;filename=" + resource.getFilename())
                .body(resource);
    }

}
