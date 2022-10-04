package org.dbilik.fileServer.config.resources.properties;

import org.dbilik.fileServer.config.resources.resource.ResourceUtils;
import org.dbilik.fileServer.utils.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.dbilik.fileServer.config.resources.properties.PropertiesProvider.getProperties;
import static org.dbilik.fileServer.config.resources.resource.ResourceUtils.*;
import static org.dbilik.fileServer.utils.ArrayUtils.isEmpty;

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
 * Fore now ... I have no usage for this ... Just wanted to try to make it
 *
 * TODO: enable type 'properties' and 'yaml' for one resource. So, the default resource can be, for example yaml, the rest, for example properties. It's a minor but foolproof feature
 * TODO: if all of the resources are of type YAML, just gather them and put into YamlPropertiesFactoryBean at once with setting overriding option - if first todo will be done, this will need more lines to implement
 *
 */
public class MultiProfilePropertySourceFactory implements PropertySourceFactory, SpringApplicationRunListener {

    private static final Logger logger = LoggerFactory.getLogger(MultiProfilePropertySourceFactory.class);

    private static final String DEFAULT_RESOURCE = "default";

    private static String[] activeProfiles;

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

    /**
     * Why 'protected' ? ... testing this with application context would be hard. If we wanted to change active profiles,
     * or propertySource name (to cover all possible scenarios), we would have to load application context over and over and
     * that would be expensive. This way, we can test just the main method of property loading,
     * which is actually everything we need, becasue there is not any more logic in 'createPropertySource' method.
     *
     * Honestly, this is not a business logic. So after properly testing it once, we can actually delete all tests...
     */
    protected Properties loadAllProperties(Resource resource) throws IOException {
        logger.debug("Loading all properties based on active profiles for resource {}", resource.getFilename());

        Map<String, Properties> propertiesMap = new HashMap<>();

        Properties defaultProperties = resolveResource(resource); // first we load properties from default resource
        String profileOfDefaultResource = getResourceProfile(resource.getFilename());
        if (profileOfDefaultResource == null) {
            propertiesMap.put(DEFAULT_RESOURCE, defaultProperties);
        } else {
            throw new RuntimeException("You can not define profile  as default resource. Implement this scenario if you want to do that.");
        }

        if (activeProfiles == null || activeProfiles.length == 0) {
            logger.debug("Only default resource is used [{}], because there is 0 active profiles", resource.getFilename());
            Properties properties = (Properties) propertiesMap.values().toArray()[0];
            logFinalProperties(properties, resource.getFilename());
            return properties;
        }

        for (String profile : activeProfiles) {
            Resource currentResource = null;
            if (profile.equals(ResourceUtils.getResourceProfile(resource.getFilename()))) {
                continue; // if default resource already has a profile, skip it because it was already added
            } else {
                try {
                    currentResource = loadResourceFromURI(replaceProfile(resource.getURI(), profile));
                } catch (FileNotFoundException ex) {
                    logger.debug("Error occured while trying to load resource {} for profile {}. Probably missing file for particular profile, so we just continue", resource.getURI(), profile);
                } catch (IOException ex) {
                    throw new RuntimeException("Exception occured while loading a resource", ex);
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(String.format("Replacing profile [%s] in URI [{}] threw exception", profile, resource.getURI()), ex);
                }
            }

            if (currentResource != null) {
                propertiesMap.put(profile, getProperties(currentResource));
            }
        }

        // merge it together -> doing it this way, because default resource might already have profile,which does not have to be defined as first, so we do not want to override it
        Properties unitedProperties = mergeProperties(propertiesMap);

        logFinalProperties(unitedProperties, resource.getFilename());
        return unitedProperties;
    }

    private void logFinalProperties(Properties properties, String resourceFileName) {
        StringBuilder stringBuilder = new StringBuilder("Final loaded properties for resource " + resourceFileName + ": [\n");
        properties.entrySet().forEach(entry -> stringBuilder.append("    " + entry.getKey() + " = " + entry.getValue() + "\n"));
        stringBuilder.append("]");
        logger.debug("Logging final loaded properties: \n {}", stringBuilder);
    }

    /**
     * merge it together -> doing it this way, because default resource might already have profile,
     * which does not have to be defined as first, so we do not want to override it
     */
    private Properties mergeProperties(Map<String, Properties> propertiesMap) {
        Properties props = new Properties();

        if (propertiesMap.containsKey(DEFAULT_RESOURCE)) {
            props.putAll(propertiesMap.get(DEFAULT_RESOURCE));
        }

        if (!isEmpty(activeProfiles)) {
            Arrays.stream(activeProfiles)
                    .filter(propertiesMap::containsKey)
                    .map(propertiesMap::get)
                    .forEach(props::putAll);
        }

        return props;
    }

}
