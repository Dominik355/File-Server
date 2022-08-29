package org.dbilik.fileServer.config.resources.properties;

import org.dbilik.fileServer.config.resources.resource.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MissingRequiredPropertiesException;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.IntStream;

import static org.dbilik.fileServer.config.resources.properties.PropertiesProvider.getProperties;

/**
 * This class allows us to use multiple profile based "application" files at the same time.
 *
 * Basic Spring-boot approac hwould be something like: @PropertySource({"classpath:application-${activeProfile}.properties"})
 * With this approach we can use multiple profiles at the same time and take control over which parameter to use.
 * Let's say we want our develop profile for testing, but at the same time we want to mock DB with Mock profile.
 *
 * Keep in mind, that if you are using multiple resources, which share same keys.
 * Value from second declared profile will be used.
 * So basically said: latest value overrides.
 *
 * Fore now ... I have no usage for this ... Just wanted to try to make it :)
 */
public class MultiProfilePropertySourceFactory implements PropertySourceFactory, SpringApplicationRunListener {

    public static final Logger logger = LoggerFactory.getLogger(MultiProfilePropertySourceFactory.class);

    private static String[] activeProfiles = new String[0];

    // Default constructor for PropertySourceFactory
    public MultiProfilePropertySourceFactory() {
    }

    // Constructor used by SpringApplicationRunListener
    public MultiProfilePropertySourceFactory(SpringApplication app, String[] params) {
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        activeProfiles = environment.getActiveProfiles();
        logger.info("These property sources were loaded: {}", environment.getPropertySources().stream().map(prop -> prop.getName()).toList());
        logger.info("Active profiles are: {}", Arrays.toString(activeProfiles));
    }


    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        logger.info("Creating properties from resource {}. Active profiles: {}", resource.getResource().getURI(), Arrays.toString(activeProfiles));
        return new PropertiesPropertySource(
                name != null ? name : resolveCommonResourceName(resource.getResource().getFilename()),
                loadAllProperties(resource.getResource()));
    }

    private Properties loadAllProperties(Resource resource) throws IOException {
        logger.debug("Loading all properties based on active profiles for resource {}", resource.getFilename());
        Properties unitedProperties = resolveResource(resource); // first we load properties from default resource

        if (activeProfiles == null || activeProfiles.length == 0) {
            logger.warn("Only default resource is used [{}], because there is 0 active profiles", resource.getFilename());
            return unitedProperties;
        }

        for (String profile : activeProfiles) {
            Resource currentResource;
            if (profile.equals(getResourceProfile(resource.getFilename()))) {
                continue; // if default resource already has a profile, skip it because it was already added
            } else {
                try {
                    currentResource = ResourceLoader.loadResourceFromURI(replaceProfile(resource.getURI(), profile));
                } catch (Exception ex) {
                    throw new RuntimeException(String.format("Replacing profile [%s] in URI [{}] threw exception", profile, resource.getURI().getPath()), ex);
                }
            }

            unitedProperties.putAll(getProperties(currentResource));
        }

        logFinalProperties(unitedProperties);
        return unitedProperties;
    }

    private Properties resolveResource(Resource resource) {
        if (ResourceLoader.isResourceYaml(resource)) {
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
     * Resource name has a patter "[name]-[profile].[type]"
     * type is either yaml/yml or properties
     */
    private String getResourceProfile(String resourceName) {
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

    private URI replaceProfile(URI uri, String profile) throws URISyntaxException {
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
        } else {
            // because profile name can be used anywhere in path, we add dot to make sure we replace right part
            newPath= uri.getPath().replace(uriProfile + ".", profile + ".");
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
    private String resolveCommonResourceName(String resourceName) {
        String profile = getResourceProfile(resourceName);
        if (profile == null) {
            return resourceName.split("\\.")[0];
        }
        return resourceName.split("-" + profile)[0];
    }

    private void logFinalProperties(Properties properties) {
        StringBuilder stringBuilder = new StringBuilder();
        properties.entrySet().forEach(entry -> stringBuilder.append(entry.getKey() + " = " + entry.getValue()));
        logger.debug("Logging final loaded properties: \n {}", stringBuilder.toString());
    }


}
