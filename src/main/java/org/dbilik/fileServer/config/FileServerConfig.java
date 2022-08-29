package org.dbilik.fileServer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationPropertiesScan(basePackages = {"org.dbilik.fileServer.config.resources"})
public class FileServerConfig {

    private static final Logger log = LoggerFactory.getLogger(FileServerConfig.class);

    public Resource loadResourceFile(String name) {


        return null;
    }

}
