package org.dbilik.fileServer.service;

import java.nio.file.Path;

public interface AbstractIOFileService {

    /**
     *
     * @param dir - directory, which should be created
     * @param createParents - if TRUE, then create all non existing parent directories also
     * @return
     */
    Path createDirectory(Path dir, Boolean createParents);

    /**
     *
     * @param dir - directory, which should be deleted
     * @param deleteRecursively - if TRUE, then delete directory even if not empty
     * @return
     */
    Boolean deleteDirectory(Path dir, Boolean deleteRecursively);

    Path renameDirectory(Path dir, String oldName, String newName);

    Path moveDirectory(Path originalPath, Path newPath);

    Path copyDirectory(Path originalPath, Path newPath);

    // loadDirectory(Path dir);
    // returns tree of that directory (metadata of files and sub-directories)
    /*
    rename, move and copy might be connected into 1 method in implementation
    Just add 1 new attribute : Boolean deleteOriginal (FALSE for copy, TRUE for move and rename)
     */


    // THIS FILE STUFF MIGHT BE IN SEPARATE SERVICE ALSO
    // - 1 FOR DIRECTORY HANDLING, 1 FOR FILE HANDLING ... SHALL SEE, KEEP IT IN ONE FOR NOW

    Path createFile(Path path);

    Boolean deleteFile(Path path);

    Path renameFile(Path dir, String oldName, String newName);

    Path moveFile(Path originalPath, Path newPath);

    Path copyFile(Path originalPath, Path newPath);

    // will use createFile
    // Path replaceFile(Path originalPath,)

    // AND OF COURSE
    // we have createfile mthod, but it creates just an empty file
    // we need to put data in there.. so when i figure out right parameter(if inputstream or byte[] ... etc), i will add it.

    // now listing files, with walk tree - probably same as loadDirectory ???


    // also need to implement find methods - using walk tree in Files

    /**
     *
     * @param path - known part of path
     * @param fileName - name of file or directory
     * @param includeDirectories - if TRUE, include directories also (default TRUE)
     * @return
     */
    Path findFile(Path path, String fileName, Boolean includeDirectories);

}
