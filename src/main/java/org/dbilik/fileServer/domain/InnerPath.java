package org.dbilik.fileServer.domain;

/**
 * because every User works just with his subdirectory,
 * there is no need to define full Path.
 * Full Path will be filled somewhere in WebFileService. Again IOFileService shouldnt handle this.
 * SERVER_WORKING_PATH is server property and we dont wanna these dependencies inside IO service.
 * So some converter class, that converts InnerPath to Path with adding SERVER_WORKING_PATH should
 * be created and used before calling IO service. Also Path from IOService should be converted
 * to this class, os it can be presented to user without revealing intra info of server.
 * So this class defines just a part of Path specific for that user
 */
public class InnerPath {

    private String userIdentifier;
    private String path;

    private InnerPath(String userIdentifier, String path) {
        this.userIdentifier = userIdentifier;
        this.path = path;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public String getPath() {
        return path;
    }

    public static InnerPath of(String userIdentifier, String path) {
        return new InnerPath(userIdentifier, path);
    }

    public static InnerPath of(String... parts) {
        if (parts.length > 2) {
            throw new IllegalArgumentException("There can not be more than 2 parts to create InnerPath");
        }
        return of(parts[0], parts[1]);
    }

}
