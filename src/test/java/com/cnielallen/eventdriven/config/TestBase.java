package com.cnielallen.eventdriven.config;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Slf4j
@ExtendWith(ContainerEnvironmentExtension.class)
@ResourceLock(Environment.ID)
public abstract class TestBase {


    @BeforeAll
    static void prepareContainerEnvironment(Environment resource) {

    }


    @DynamicPropertySource
    static void dynamicPropertySource(DynamicPropertyRegistry registry) {

    }
}
