package org.dbilik.fileServer.config.resources.resource;

import org.dbilik.fileServer.abstractt.AbstractTest;
import org.dbilik.fileServer.service.exception.FileNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceUtilsTest extends AbstractTest {

    @Test
    void loadResource_NullInput() {
        assertThrows(IllegalArgumentException.class,
                () -> ResourceUtils.loadResource(null));
    }

    @Test
    void loadResource_EmptyString() {
        assertThrows(FileNotFoundException.class,
                () -> ResourceUtils.loadResource(" "));
    }

    @Test
    public void loadResource_NonExisting() throws IOException {
        assertThrows(FileNotFoundException.class,
                () -> ResourceUtils.loadResource("/somewhere/over/the/rainbow/config.properties"));

    }

    @Test
    void loadResource_ResourceExists_absolutePath() throws IOException {
        Resource resource = ResourceUtils.loadResource("/serverProperties/server_configuration.yml");

        assertResource(resource, "server_configuration.properties");
    }

    @Test
    void loadResource_ResourceExists_relativePath() throws IOException {
        Resource resource = ResourceUtils.loadResource("serverProperties/server_configuration.yml");

        assertResource(resource, "server_configuration.properties");
    }

    @ParameterizedTest
    @ValueSource(strings = {"yml", "yaml", "YML", "YaMl"})
    void isResourceYamlTest(String extension) {
        assertTrue(ResourceUtils.isResourceYaml(new ClassPathResource("/something/props." + extension)));
    }

    private void assertResource(Resource resource, String fileName) throws IOException {
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
        assertTrue(resource.getFilename().equals(fileName));
        assertTrue(resource.contentLength() > 0);
        assertTrue(!new String(resource.getInputStream().readNBytes(128), StandardCharsets.UTF_8).isEmpty());
    }

}
