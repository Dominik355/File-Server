package org.dbilik.fileServer.domain;

import java.time.LocalDateTime;

public class Directory {

    private Directory parent;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime lastModified;
    private String owner; // just a reminder for now that every directory will have an owner for granting access to others

}
