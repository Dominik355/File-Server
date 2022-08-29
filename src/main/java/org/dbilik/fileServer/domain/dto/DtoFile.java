package org.dbilik.fileServer.domain.dto;

import org.dbilik.fileServer.FilePointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.util.UUID;

public class DtoFile {
    
    private static final Logger log = LoggerFactory.getLogger(DtoFile.class);

    private final HttpMethod method;
    private final FilePointer filePointer;
    private final UUID uuid;

    public DtoFile(HttpMethod method, FilePointer filePointer, UUID uuid) {
        this.method = method;
        this.filePointer = filePointer;
        this.uuid = uuid;
    }


}
