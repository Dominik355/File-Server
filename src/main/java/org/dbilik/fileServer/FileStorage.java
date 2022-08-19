package org.dbilik.fileServer;

import java.util.Optional;

public interface FileStorage {

    Optional<FilePointer> findFile(String fileName);

}
