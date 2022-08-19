package org.dbilik.fileServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SystemFilePointer implements FilePointer {

    private static final Logger log = LoggerFactory.getLogger(SystemFilePointer.class);

    private final File target;
    private String name;

    public SystemFilePointer(File target, String name) {
        this.target = target;
        this.name = name;
    }

    @Override
    public InputStream open() {
        log.debug("Opening an InputStream for file {}", target.getName());
        try {
            return new BufferedInputStream(new FileInputStream(target));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public File getFile() {
        log.debug("Returning a file {}", target.getName());
        return this.target;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
