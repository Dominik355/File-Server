package org.dbilik.fileServer.config.resources.properties;

import java.util.regex.Pattern;

public final class SystemProperties {

    public static final SystemProperties props = new SystemProperties();

    public String operatingsystem;
    public String version;
    public int majorVersion;
    public int minorVersion;
    public int availableProcessors;

    private SystemProperties() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("win") >= 0) {
            operatingsystem = Platform.WINDOWS.name();
        }
        else if (os.indexOf("nux") >= 0 || os.indexOf("nix") >= 0) {
            operatingsystem = Platform.LINUX.name();
        }
        else if (os.indexOf("mac") >= 0) {
            operatingsystem = Platform.MAC.name();
        }
        else {
            operatingsystem = Platform.UNKNOWN.name();
        }

        version = System.getProperty("os.version");
        String[] parts = version.split(Pattern.quote("."));

        if (parts.length > 0) {
            majorVersion = Integer.parseInt(parts[0]);

            if (parts.length > 1) {
                minorVersion = Integer.parseInt(parts[1]);
            }
            else {
                minorVersion = -1;
            }
        }
        else {
            majorVersion = -1;
            minorVersion = -1;
        }

        availableProcessors = Runtime.getRuntime().availableProcessors();
    }

    public boolean isWindows() {
        return Platform.WINDOWS.name().equals(operatingsystem);
    }

    public boolean isLinux() {
        return Platform.LINUX.name().equals(operatingsystem);
    }

    public boolean isMac() {
        return Platform.MAC.name().equals(operatingsystem);
    }

    private enum Platform {UNKNOWN, WINDOWS, LINUX, MAC}

}
