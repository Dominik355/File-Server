package org.dbilik.fileServer.io;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

public class ResourceLoaderTest {

    @Test
    public void test1() {
        gett();
    }

    private void gett() {
        String os = System.getProperty("os.name").toLowerCase();

        String platform;
        int majorVersion;
        int minorVersion;

        if (os.indexOf("win") >= 0) {
            platform = "WINDOWS";
        }
        else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
            platform = "LINUX";
        }
        else if (os.indexOf("mac") >= 0) {
            platform = "MAC";
        }
        else {
            platform = "UNKNOWN";
        }

        String version = System.getProperty("os.version").toString();
        System.out.println("vertsion: " + version);
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

        System.out.println("system: " + platform);
        System.out.println("minor: " + minorVersion);
        System.out.println("major: " + majorVersion);
    }

}
