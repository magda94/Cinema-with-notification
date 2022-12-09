package com.film.service.container;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ContextConfiguration(initializers = MongoDbContainer.DataSourceInitializer.class)
public class MongoDbContainer {

    @Container
    private static final MongoDBContainer database = new MongoDBContainer(DockerImageName.parse("mongo:4.4.18"))
            .withExposedPorts(27017)
            .withReuse(true);

    public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>  {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.data.mongodb.uri=" + database.getReplicaSetUrl());
        }
    }
}
