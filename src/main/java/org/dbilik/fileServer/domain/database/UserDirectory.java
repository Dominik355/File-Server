package org.dbilik.fileServer.domain.database;

public class UserDirectory {

    private Long userId;
    // identifier for user directory, not related to userId
    // might be the same, but do not treat them as the same
    // point is to separate authentication with filesystem itself
    private String userDirectoryId;

}
