package org.dbilik.fileServer.config.resources.properties;

import org.dbilik.fileServer.abstractt.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

@EnableConfigurationProperties(value = FileServerProperties.class)
public class MultiProfilePropertiesTest extends AbstractIntegrationTest {

    @Autowired
    private FileServerProperties serverProperties;

    @ComponentScan(excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = PropertiesConfiguration.FileServerPropertiesConfiguration.class)
    }) // now we are using same property source as in code. But if placement or name of files changes in code, it should not affect tests
    @PropertySource(value = "classpath:serverProperties/server_configuration.yml", factory = MultiProfilePropertySourceFactory.class)
    private static class InnerConfiguration {

    }

    @Test
    public void missingResourceFile_format_properties() {}
    @Test
    public void incompleteResourceFile_format_properties() {}
    @Test
    public void nonExistingProfile_format_properties() {}

    @Test
    public void activeProfiles_None_format_properties() {}
    @Test
    public void activeProfiles_Develop_format_properties() {}
    @Test
    public void activeProfiles_Mock_format_properties() {}
    @Test
    public void activeProfiles_MockDevelop_format_properties() {}


    @Test
    public void missingResourceFile_format_yml() {}
    @Test
    public void incompleteResourceFile_format_yml() {}
    @Test
    public void nonExistingProfile_format_yml() {}

    @Test
    public void activeProfiles_None_format_yml() {}
    @Test
    public void activeProfiles_Develop_format_yml() {}
    @Test
    public void activeProfiles_Mock_format_yml() {}
    @Test
    public void activeProfiles_MockDevelop_format_yml() {}

    @Test
    public void defaultIsYml_otherAreProperties() {}

}
