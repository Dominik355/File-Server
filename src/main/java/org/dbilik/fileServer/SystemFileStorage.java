package org.dbilik.fileServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

@Component
@Profile("!test")
public class SystemFileStorage implements FileStorage {

    private static final Logger log = LoggerFactory.getLogger(SystemFileStorage.class);

    private static final String STORAGE_PARENT_FILE_PREFIX = "/fileStorage/";

    @Override
    public Optional<FilePointer> findFile(String fileName) {
        log.debug("FileSystemStorage.findFile({})", fileName);
        return Optional.of(
                new SystemFilePointer(
                        new File(getClass()
                                .getResource(STORAGE_PARENT_FILE_PREFIX + fileName)
                                .getFile()
                        ),
                        fileName
                ));
    }

}
