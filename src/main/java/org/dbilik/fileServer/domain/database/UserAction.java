package org.dbilik.fileServer.domain.database;

import java.time.LocalDateTime;

/**
 * keep track of all user actions in filesystem - record per action
 *
 */
public class UserAction {

    private String actionType;
    private LocalDateTime dateTime;
    private String fullPath; // path or path with file - based on action

}
