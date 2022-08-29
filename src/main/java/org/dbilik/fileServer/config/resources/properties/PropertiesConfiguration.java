package org.dbilik.fileServer.config.resources.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class PropertiesConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesConfiguration.class);

    /**
     * Because we are using MultiProfilePropertySourceFactory means, we do not have to define property resource for every profile,
     * or add dynamic spring.active.profile parameter into value of PropertySource. It takes care of that
     */
    @Configuration
    @PropertySource(value = "classpath:serverProperties/server_configuration.yml", factory = MultiProfilePropertySourceFactory.class)
    public static class FileServerConfigProperties {

    }

}
