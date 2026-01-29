package com.weather.demo.initializer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

public class TestContainersInitialization implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final PostgreSQLContainer<?> POSTGRES_SQL_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:latest")).withDatabaseName("postgres").withUrlParam("stringtype", "unspecified")
            .withLogConsumer(new StdOutConsumer<>("postgres:latest"));

    static {
        Startables.deepStart(POSTGRES_SQL_CONTAINER).join();
    }

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {

        TestPropertyValues.of("spring.datasource.url=" + POSTGRES_SQL_CONTAINER.getJdbcUrl(),
                "spring.datasource.username=" + POSTGRES_SQL_CONTAINER.getUsername(),
                "spring.datasource.password=" + POSTGRES_SQL_CONTAINER.getPassword(),
                "spring.datasource.driver-class-name=" + POSTGRES_SQL_CONTAINER.getDriverClassName());
    }
}
