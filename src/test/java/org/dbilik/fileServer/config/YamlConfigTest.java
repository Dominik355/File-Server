package org.dbilik.fileServer.config;

import org.dbilik.fileServer.abstractt.AbstractIntegrationTest;
import org.dbilik.fileServer.config.resources.properties.FileServerProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@EnableConfigurationProperties(value = FileServerProperties.class) //Component scan won't be enoguht, becasue this adds more beans , see @EnableConfigurationPropertiesRegistrar
@TestPropertySource("classpath:server_configuration.yml") // This overrides property file name defined in tested class. In both ways... file have to be included in test resources
public class YamlConfigTest extends AbstractIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FileServerProperties properties;

    @PostConstruct
    public void printBeans() {
        System.out.println("\n************************************************************\n");
        Arrays.stream(applicationContext.getBeanDefinitionNames())
                .forEach(System.out::println);
        System.out.println("\n************************************************************\n");
    }

    @Test
    public void whenFactoryProvidedThenYamlPropertiesInjected() {
        System.out.println("getFileServerDirectory: " + properties.workingPath());
        assertThat(properties.workingPath()).isNotBlank();
    }

    @Test
    public void fetchProperties() {
        Properties properties = new Properties();
        try {
            File file = ResourceUtils.getFile("classpath:custom_config-test.yml");
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
            System.out.println("error: \n" + e);
        }

        System.out.println("property: " + properties.getProperty("over.here"));
    }

}
