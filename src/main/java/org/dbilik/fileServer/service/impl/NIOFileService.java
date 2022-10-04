package org.dbilik.fileServer.service.impl;

import org.dbilik.fileServer.service.AbstractIOFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class NIOFileService implements AbstractIOFileService {

    private static final Logger log = LoggerFactory.getLogger(NIOFileService.class);

    @Override
    public Path createDirectory(Path dir, Boolean createParents) {
        return null;
    }

    @Override
    public Boolean deleteDirectory(Path dir, Boolean deleteRecursively) {
        return null;
    }

    @Override
    public Path renameDirectory(Path dir, String oldName, String newName) {
        return null;
    }

    @Override
    public Path moveDirectory(Path originalPath, Path newPath) {
        return null;
    }

    @Override
    public Path copyDirectory(Path originalPath, Path newPath) {
        return null;
    }

    @Override
    public Path createFile(Path path) {
        return null;
    }

    @Override
    public Boolean deleteFile(Path path) {
        return null;
    }

    @Override
    public Path renameFile(Path dir, String oldName, String newName) {
        return null;
    }

    @Override
    public Path moveFile(Path originalPath, Path newPath) {
        return null;
    }

    @Override
    public Path copyFile(Path originalPath, Path newPath) {
        return null;
    }

    @Override
    public Path findFile(Path path, String fileName, Boolean includeDirectories) {
        return null;
    }
}
