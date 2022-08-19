package org.dbilik.fileServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.dbilik.fileServer.FileTestUtils.TXT_FILE;
import static org.dbilik.fileServer.FileTestUtils.TXT_FILE_NAME;

@Component
@Profile("test")
public class FileStorageStub implements FileStorage {

    private static final Logger log = LoggerFactory.getLogger(FileStorageStub.class);

    @Override
    public Optional<FilePointer> findFile(String fileName) {
        log.debug("downloading file {}", fileName);
        if (fileName.equals(TXT_FILE_NAME)) {
            return Optional.of(TXT_FILE);
        }
        return Optional.empty();
    }
}
