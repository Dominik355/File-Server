package org.dbilik.fileServer.config.resources.properties;

import java.util.regex.Pattern;

public final class SystemProperties {

    private final String operatingsystem;
    private final String version;
    private final int majorVersion;
    private final int minorVersion;

    private SystemProperties(String operatingsystem, String version, int majorVersion, int minorVersion) {
        this.operatingsystem = operatingsystem;
        this.version = version;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    public String getOperatingsystem() {
        return operatingsystem;
    }

    public String getVersion() {
        return version;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
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

    private static SystemProperties initializeInstance() {
        String platform, version;
        int minorVersion, majorVersion;

        String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("win") >= 0) {
            platform = Platform.WINDOWS.name();
        }
        else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
            platform = Platform.LINUX.name();
        }
        else if (os.indexOf("mac") >= 0) {
            platform = Platform.MAC.name();
        }
        else {
            platform = Platform.UNKNOWN.name();
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

        return new SystemProperties(platform, version, majorVersion, minorVersion);
    }

    enum Platform {UNKNOWN, WINDOWS, LINUX, MAC}

    /**
     * Lazy loaded singleton
     */
    private static class LazyHolder {
        static final SystemProperties INSTANCE = initializeInstance();
    }

    public static SystemProperties getInstance() {
        return LazyHolder.INSTANCE;
    }
}
