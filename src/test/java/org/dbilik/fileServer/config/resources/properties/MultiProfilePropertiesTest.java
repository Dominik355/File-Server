package org.dbilik.fileServer.config.resources.properties;

import org.dbilik.fileServer.abstractt.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EnableConfigurationProperties(value = FileServerProperties.class)
@ContextConfiguration(classes = {MultiProfilePropertiesTest.InnerConfiguration.class})
public class MultiProfilePropertiesTest extends AbstractIntegrationTest {

    private static final Resource DEFAULT_RESOURCE_YML;
    private static final Resource DEFAULT_RESOURCE_PROPERTIES;

    static {
        DEFAULT_RESOURCE_YML = new ClassPathResource("serverProperties/yml/server_configuration.yml");
        DEFAULT_RESOURCE_PROPERTIES = new ClassPathResource("serverProperties/properties/server_configuration.properties");
    }

    @Configuration
    @ComponentScan(excludeFilters = {
                    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = PropertiesConfiguration.class),
                    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = PropertiesConfiguration.FileServerPropertiesConfiguration.class)
                }, basePackages = "org.dbilik.fileServer.config")
    static class InnerConfiguration {

    }

    // TESTS

    // PROPERTIES
    @Test
    public void nonExistingProfile_format_properties() throws IOException {
        // given
        MultiProfilePropertySourceFactory propertySourceFactory = getInstance("NonExistingProfile");

        // when
        Properties properties = propertySourceFactory.loadAllProperties(DEFAULT_RESOURCE_PROPERTIES);

        // then
        doBasicAssertions(properties, "none", "default");
    }

    @Test
    public void activeProfiles_Develop_format_properties() throws IOException {
        // given
        MultiProfilePropertySourceFactory propertySourceFactory = getInstance("develop");

        // when
        Properties properties = propertySourceFactory.loadAllProperties(DEFAULT_RESOURCE_PROPERTIES);

        // then
        doBasicAssertions(properties, "develop", "develop");
    }

    @Test
    public void activeProfiles_Mock_format_properties() throws IOException {
        // given
        MultiProfilePropertySourceFactory propertySourceFactory = getInstance("mock");

        // when
        Properties properties = propertySourceFactory.loadAllProperties(DEFAULT_RESOURCE_PROPERTIES);

        // then
        doBasicAssertions(properties, "mock", "mock");
    }

    @Test
    public void activeProfiles_MockDevelop_format_properties() throws IOException {
        // given
        MultiProfilePropertySourceFactory propertySourceFactory = getInstance("mock", "develop");

        // when
        Properties properties = propertySourceFactory.loadAllProperties(DEFAULT_RESOURCE_PROPERTIES);

        // then
        doBasicAssertions(properties, "develop", "develop");
    }

    @Test
    public void activeProfiles_DevelopMock_format_properties() throws IOException {
        // given
        MultiProfilePropertySourceFactory propertySourceFactory = getInstance("develop", "mock");

        // when
        Properties properties = propertySourceFactory.loadAllProperties(DEFAULT_RESOURCE_PROPERTIES);

        // then
        doBasicAssertions(properties, "mock", "mock");
        assertTrue(properties.get("unique").equals("UniquePropertyForDevelopProfileToTestOerridingValues"));
    }

    // YAML
    @Test
    public void nonExistingProfile_format_yml() throws IOException {
        // given
        MultiProfilePropertySourceFactory propertySourceFactory = getInstance("NonExistingProfile");

        // when
        Properties properties = propertySourceFactory.loadAllProperties(DEFAULT_RESOURCE_YML);

        // then
        doBasicAssertions(properties, "none", "default");
    }

    @Test
    public void activeProfiles_Develop_format_yml() throws IOException {
        // given
        MultiProfilePropertySourceFactory propertySourceFactory = getInstance("develop");

        // when
        Properties properties = propertySourceFactory.loadAllProperties(DEFAULT_RESOURCE_YML);

        // then
        doBasicAssertions(properties, "develop", "develop");
    }

    @Test
    public void activeProfiles_Mock_format_yml() throws IOException {
        // given
        MultiProfilePropertySourceFactory propertySourceFactory = getInstance("mock");

        // when
        Properties properties = propertySourceFactory.loadAllProperties(DEFAULT_RESOURCE_YML);

        // then
        doBasicAssertions(properties, "mock", "mock");
    }

    @Test
    public void activeProfiles_MockDevelop_format_yml() throws IOException {
        // given
        MultiProfilePropertySourceFactory propertySourceFactory = getInstance("mock", "develop");

        // when
        Properties properties = propertySourceFactory.loadAllProperties(DEFAULT_RESOURCE_YML);

        // then
        doBasicAssertions(properties, "develop", "develop");
    }

    @Test
    public void activeProfiles_DevelopMock_format_yml() throws IOException {
        // given
        MultiProfilePropertySourceFactory propertySourceFactory = getInstance("develop", "mock");

        // when
        Properties properties = propertySourceFactory.loadAllProperties(DEFAULT_RESOURCE_YML);

        // then
        doBasicAssertions(properties, "mock", "mock");
        assertTrue(properties.get("unique").equals("UniquePropertyForDevelopProfileToTestOerridingValues"));
    }

    // OTHER TESTS
    @Test
    public void defaultResourceAlreadyHasProfile() throws IOException {
        // given
        MultiProfilePropertySourceFactory propertySourceFactory = getInstance("develop");

        // when
        assertThrows(RuntimeException.class,
                () -> propertySourceFactory
                .loadAllProperties(new ClassPathResource("serverProperties/yml/server_configuration-develop.yml")));
    }

    //  HELPER METHODS
    private MultiProfilePropertySourceFactory getInstance(String... profiles) {
        MultiProfilePropertySourceFactory propertySourceFactory = new MultiProfilePropertySourceFactory();
        setProfiles(propertySourceFactory, profiles);

        return propertySourceFactory;
    }

    private void setProfiles(MultiProfilePropertySourceFactory properties, String[] profiles) {
        try {
            Field instance = MultiProfilePropertySourceFactory.class.getDeclaredField("activeProfiles");
            instance.setAccessible(true);
            instance.set(properties, profiles);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doBasicAssertions(Properties properties, String finalProfile, String pathContains) {
        assertTrue(properties.getProperty("profile").equals(finalProfile));
        assertTrue(properties.getProperty("server.workingPath").contains(pathContains));
    }

}
