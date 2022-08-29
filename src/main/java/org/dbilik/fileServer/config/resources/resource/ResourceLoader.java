package org.dbilik.fileServer.config.resources.resource;

import org.dbilik.fileServer.service.exception.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.net.URI;

public class ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

    public static final String RESOURCE_FILE_SCHEME = "file";
    public static final String RESOURCE_CLASSPATH_SCHEME= "classpath";

    /**
     * load resource from classpath and throw exception if not found
     * @param path - absolute or relative path of resource
     * @return found Resource
     */
    public static Resource loadResource(String path) {
        try {
            return new ClassPathResource(path, ClassLoader.getSystemClassLoader());
        } catch (FileNotFoundException ex) {
            logger.error("resource[{}] was not found in the classpath, let's throw an exception, becasue we are obviously missing some file");
            throw ex;
        }
    }

    /**
     * same method "loadResource" is used 3 times here. We could get regulate that,
     * but for better understandability, let's keep it that way.
     */
    public static Resource loadResourceFromURI(URI uri) {
        logger.debug("getResourceFromURI called for URI = {}", uri);

        if (RESOURCE_FILE_SCHEME.equalsIgnoreCase(uri.getScheme())) {
            // if path starts with file, we try to load it primarily like resource, secondary as system file
            try {
                return loadResource(uri.getPath().split("classes/", 2)[1]);
            } catch (Exception e) {
                logger.debug("No luck loading this uri [{}] from classpath", uri);
            }
            return new FileSystemResource(uri.getPath());

        } else if (RESOURCE_CLASSPATH_SCHEME.equalsIgnoreCase(uri.getScheme())) {
            logger.debug("URI has a classpath scheme, so we try to get it with classloader");
            return loadResource(uri.getPath());
        }

        logger.debug("URI has no prefix, so we getting it with classloader");
        return loadResource(uri.getPath());
    }

    public static boolean isResourceYaml(Resource resource) {
        String extenstion;
        try {
            String[] splitted = resource.getFilename().split("\\.");
            extenstion = splitted[splitted.length - 1];
        } catch (Exception e) {
            throw new RuntimeException("We were not able to get extension of resource: " + resource.getFilename(), e);
        }
        return extenstion.equalsIgnoreCase("yaml") || extenstion.equalsIgnoreCase("yml");
    }

}
