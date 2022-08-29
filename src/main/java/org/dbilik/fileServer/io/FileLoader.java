package org.dbilik.fileServer.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class responsible for handling IO stuff using java.nio API
 */
public class FileLoader {

    private static final Logger log = LoggerFactory.getLogger(FileLoader.class);

    public void doo() {
        findfile("");
    }

    //find file
    public Path findfile(String relativePath) {

        return null;
    }

    /**
     * If the string does not start with a slash, then add it
     */
    private String prefixStringWithSlash(String s) {
        return !s.startsWith("/") ? "/" + s : s;
    }

    private void getPathInfo(Path path) {
        System.out.println("\n-----------------------------------------------------\n");
        System.out.println("isRegularFile: " + Files.isRegularFile(path));
        System.out.println("isDirectory: " + Files.isDirectory(path));
        System.out.println("isWritable: " + Files.isWritable(path));
        System.out.println("getFileName: " + path.getFileName());
        System.out.println("getParent: " + path.getParent());
        System.out.println("getRoot: " + path.getRoot());
        System.out.println("getFileSystem: " + path.getFileSystem());
        System.out.println("getRootDirectories: " + path.getFileSystem().getRootDirectories());
    }

}
