package org.dbilik.fileServer.domain;

import java.nio.file.Path;

import static org.dbilik.fileServer.utils.PathNameUtils.addLeading;
import static org.dbilik.fileServer.utils.PathNameUtils.removeLeading;

public final class PathConverter {

    private final String pathPrefix;

    public PathConverter(String pathPrefix) {
        this.pathPrefix = pathPrefix.endsWith("/") ?
                pathPrefix.substring(0, pathPrefix.length() - 1) :
                pathPrefix;
    }

    /**
     * Converts innerPath object to full Path valid in this file-system
     * @param innerPath
     * @return
     */
    public Path toPath(InnerPath innerPath) {
        return Path.of(pathPrefix + getFullPath(innerPath));
    }

    /**
     * Parse file-system Path to InnerPath object representing path info presentable outside the application
     * @param path
     * @return
     */
    public InnerPath toInnerPath(Path path) {
        String relevantPath = removeLeading(path.toString().substring(pathPrefix.length()));
        String[] parts = relevantPath.split("/", 2);
        return InnerPath.of(parts[0], parts[1]);
    }

    private String getFullPath(InnerPath innerPath) {
        return "/" + innerPath.getUserIdentifier() + addLeading(innerPath.getPath());
    }

}
