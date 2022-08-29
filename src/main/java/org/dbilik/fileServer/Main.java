package org.dbilik.fileServer;

import org.dbilik.fileServer.config.resources.properties.FileServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Main {

    @Autowired
    FileServerProperties properties;
    public static void main(String[] args) {

        new SpringApplicationBuilder(Main.class)
                .run(args);

    }

}