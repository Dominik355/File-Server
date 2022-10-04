package org.dbilik.fileServer.config.resources.properties;

import org.dbilik.fileServer.abstractt.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Properties;

public class PropertiesProviderTest extends AbstractTest {

    @Test
    public void getProperties_existingFile() throws FileNotFoundException {
        // given
        String path = "serverProperties/server_configuration.yml";

        // when
        Properties properties = PropertiesProvider.getProperties(path);

        // then
        String profile = properties.getProperty("profile");
        Assertions.assertTrue(profile.equals("none"));
    }

}
