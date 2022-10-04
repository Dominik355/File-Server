package org.dbilik.fileServer.abstractt;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Spring extenstion by itself instantiate these beans:
 * org.springframework.context.annotation.internalConfigurationAnnotationProcessor
 * org.springframework.context.annotation.internalAutowiredAnnotationProcessor
 * org.springframework.context.annotation.internalCommonAnnotationProcessor
 * org.springframework.context.event.internalEventListenerProcessor
 * org.springframework.context.event.internalEventListenerFactory
 * org.springframework.boot.test.mock.mockito.MockitoPostProcessor$SpyPostProcessor
 * org.springframework.boot.test.mock.mockito.MockitoPostProcessor
 *
 * If you do not need whole application running (no need of SpringBootTest),
 * then just extend this class and add beans you need for your test
 */
@ExtendWith(SpringExtension.class)
public abstract class AbstractIntegrationTest extends AbstractTest {

    @Autowired
    protected ApplicationContext applicationContext;

    @PostConstruct
    private void printBeans() {
        System.out.println("\n*************************************** INSTANTIATED BEANS ***************************************\n");
        Arrays.stream(applicationContext.getBeanDefinitionNames())
                .map(beanName -> !beanName.contains("springframework") ? "Local Bean: " + beanName : beanName)
                .forEach(System.out::println);
        System.out.println("\n**************************************************************************************************\n");
    }

}
