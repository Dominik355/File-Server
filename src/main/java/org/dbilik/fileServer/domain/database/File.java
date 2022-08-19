package org.dbilik.fileServer.domain.database;

import java.time.LocalDateTime;

public class File {

    private String uuid;
    private String name;
    private String path;
    private LocalDateTime creationDate;
    private LocalDateTime lastModified;
    private String extension;
    private long sizeInBytes;
    private boolean isCompressed;
    private String compressAlgorithm;


}
