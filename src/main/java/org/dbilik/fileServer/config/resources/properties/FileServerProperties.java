package org.dbilik.fileServer.config.resources.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "server")
public record FileServerProperties(String workingPath,
                                   long allowedSize,
                                   String allowedSizeUnit) {
    // record makes this class immutable, so we do not have to use constructor injection without setter methods
    // also constructor binding is not needed when there is only 1 constructor
}
