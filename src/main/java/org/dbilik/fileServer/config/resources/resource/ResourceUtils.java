package org.dbilik.fileServer.config.resources.resource;

import org.dbilik.fileServer.config.resources.properties.PropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.stream.IntStream;

public class ResourceUtils {

    private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);

    public static final String RESOURCE_FILE_SCHEME = "file";
    public static final String RESOURCE_CLASSPATH_SCHEME= "classpath";

    /**
     * load resource from classpath and throw exception if not found
     * @param path - absolute or relative path of resource
     * @return found Resource
     */
    public static Resource loadResource(String path) throws FileNotFoundException {
        Resource resource = new ClassPathResource(path, ClassLoader.getSystemClassLoader());
        if (!resource.exists()) {
            throw new FileNotFoundException("Resource with path = " + path + ", could not be found");
        }
        return resource;
    }

    public static Resource loadResourceFromURI(URI uri) throws FileNotFoundException {
        logger.debug("getResourceFromURI called for URI = {}", uri);

        return loadResource(uri.getPath().split("classes/", 2)[1]);
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

    public static Properties resolveResource(Resource resource) {
        if (ResourceUtils.isResourceYaml(resource)) {
            return PropertiesProvider.mapYamlResourceToProperties(resource);
        }
        try {
            Properties properties = new Properties();
            properties.load(resource.getInputStream());
            return properties;
        } catch (IOException e) {
            logger.error("Error occured while trying to load properties from inputstream of resource {}", resource.getFilename());
            throw new RuntimeException(e);
        }
    }

    /**
     * Resource name has a pattern "[name]-[profile].[type]"
     * type is either yaml/yml or properties
     */
    public static String getResourceProfile(String resourceName) {
        if (resourceName.contains("/")) {
            String[] splitted = resourceName.split("/");
            resourceName = splitted[splitted.length - 1];
        }
        if (!resourceName.contains("-")) {
            return null;
        }
        String[] splitted = resourceName.split("\\.");
        String[] splitted2 = splitted[0].split("-");
        return splitted2[splitted2.length - 1];
    }

    /**
     * In case you have URI with profile and you wanna get rid of that, just use profile = null
     */
    public static URI replaceProfile(URI uri, String profile) throws URISyntaxException {
        String uriProfile = getResourceProfile(uri.getPath());
        String newPath;
        if (uriProfile == null) {
            logger.debug("URI {} has no profile defined. So we add it instead", uri.getPath());
            String[] parts = uri.getPath().split("\\.");
            StringBuilder stringBuilder = new StringBuilder();
            IntStream.range(0, parts.length).forEach(i -> {
                if (i == parts.length - 1) {
                    stringBuilder.append("-" + profile + ".");
                }
                stringBuilder.append(parts[i]);
            });

            newPath = stringBuilder.toString();
        } else if (uriProfile != null && (profile != null || !profile.isEmpty())) {
            // because profile name can be used anywhere in path, we add dot to make sure we replace right part
            newPath= uri.getPath().replace(uriProfile + ".", profile + ".");
        } else {
            //uriprofile != null and profile == null - we get rid of profile and treturn default uri for resource
            newPath= uri.getPath().replace( "-" + uriProfile, "");
        }
        logger.debug("Original URI [{}] has new profile added/replaced = {}", uri.getPath(), newPath);

        return new URI(uri.getScheme(),
                uri.getHost(),
                newPath,
                uri.getFragment());
    }

    /**
     * input:                                output:
     * server_configuration.yml              server_configuration
     * server_configuration-develop.yml      server_configuration
     */
    public static String resolveCommonResourceName(String resourceName) {
        String profile = getResourceProfile(resourceName);
        if (profile == null) {
            return resourceName.split("\\.")[0];
        }
        return resourceName.split("-" + profile)[0];
    }

}
