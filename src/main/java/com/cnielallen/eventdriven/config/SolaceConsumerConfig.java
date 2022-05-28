package com.cnielallen.eventdriven.config;

import com.solace.spring.boot.autoconfigure.SolaceJavaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SolaceConsumerConfig {

    @Bean
    @Primary
    SolaceJavaProperties solaceJavaProperties() {
        return new SolaceJavaProperties() {
            @Override
            public String getClientPassword() {
                return super.getClientPassword();
            }

            @Override
            public String getClientUsername() {
                return super.getClientUsername();
            }
        };
    }
}
