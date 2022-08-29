package org.dbilik.fileServer.config.resources.properties;

import org.dbilik.fileServer.config.resources.resource.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

@Component
public class PropertiesProvider {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesProvider.class);

    public String loadProperty(String filePath, String propertyName) {
        return loadProperty(filePath, propertyName, String.class);
    }

    public <T> T loadProperty(String filePath, String propertyName, Class<T> clazz) {
        throw new UnsupportedOperationException("loading property based on name is not implemented yet");
    }

    /**
     * Return properties from resource file in classpath
     * Load .properties but also YAML file.
     * @param filePath
     * @return
     */
    public static Properties getProperties(String filePath) {
        Resource resource = ResourceLoader.loadResource(filePath);
        return getProperties(resource);
    }

    public static Properties getProperties(Resource resource) {
        Properties properties = new Properties();

        if (ResourceLoader.isResourceYaml(resource)) {
            properties = mapYamlResourceToProperties(resource);
        } else {
            try {
                properties.load(resource.getInputStream());
            } catch (IOException e) {
                logger.error("Error occured while trying to load properties from inputstream of resource {}", resource.getFilename());
                throw new RuntimeException(e);
            }
        }

        return properties;
    }

    public static Properties mapYamlResourceToProperties(EncodedResource resource) {
        return mapYamlResourceToProperties(resource.getResource());
    }

    public static Properties mapYamlResourceToProperties(Resource resource) {
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResolutionMethod(YamlProcessor.ResolutionMethod.OVERRIDE);
            factory.setResources(resource);
            factory.afterPropertiesSet();

            return factory.getObject();
        } catch (Exception ex) {
            logger.error("Exception occured on mapping YAML resource({}) to properties. Exception:\n {}", resource.getFilename(), ex);
            throw ex;
        }
    }

}
