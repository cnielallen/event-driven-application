package com.cnielallen.eventdriven.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@Getter
public class ContainerEnvironmentResource extends Environment implements ExtensionContext.Store.CloseableResource, AutoCloseable {
    private static final String DOCKER_REGISTRY = "";
    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse(DOCKER_REGISTRY + "/postgres:12.4");

    @Container
    protected static SolaceContainer solaceContainer = new SolaceContainer().withReuse(true);

    ContainerEnvironmentResource(){

    }
    @Override
    public void close(){
        log.info("Stopping all test containers.");

    }
}
