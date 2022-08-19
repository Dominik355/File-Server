package org.dbilik.fileServer;

import java.io.File;
import java.io.InputStream;

public interface FilePointer {

    public InputStream open();

    public File getFile();

    public String getName();

}
