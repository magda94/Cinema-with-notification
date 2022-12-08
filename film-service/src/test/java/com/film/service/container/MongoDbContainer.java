package com.film.service.container;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ContextConfiguration(initializers = MongoDbContainer.DataSourceInitializer.class)
public class MongoDbContainer {

    @Container
    private static final MongoDBContainer database = new MongoDBContainer(DockerImageName.parse("mongo:5.0.0"));
           // .withExposedPorts(27017);

    public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>  {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

        }
    }
}
