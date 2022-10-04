package org.dbilik.fileServer.utils;

import org.dbilik.fileServer.config.resources.properties.SystemProperties;

import java.util.Objects;

public final class PathNameUtils {

    /**
     * With the Java libraries for dealing with files, you can safely use / on all platforms.
     * The library code handles translating things into platform-specific paths internally.
     */
    private static final String FILE_SEPARATOR = "/";

    public static String addLeading(String path) {
        return path.startsWith(FILE_SEPARATOR) ?
                    path :
                    FILE_SEPARATOR + path;
    }

    public static String removeLeading(String path) {
        return path.startsWith(FILE_SEPARATOR) ?
                path.substring(1) :
                path;
    }

    public static String removeLast(String path) {
        return path.endsWith(FILE_SEPARATOR) ?
                    path.substring(0, path.length() - 1) :
                    path;
    }

    public static String toPath(boolean addLeading, String first, String... other) {
        Objects.requireNonNull(first);
        String path = first;
        if (other.length != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(first);
            for (String segment: other) {
                if (!segment.isEmpty()) {
                    if (sb.length() > 0)
                        sb.append(FILE_SEPARATOR);
                    sb.append(segment);
                }
            }
            path = sb.toString();
        }

        return addLeading ? FILE_SEPARATOR + path : path;
    }

}
