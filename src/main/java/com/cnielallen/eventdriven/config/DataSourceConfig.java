package com.cnielallen.eventdriven.config;

import com.datical.liquibase.ext.parser.LiquibaseProNamespaceDetails;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties){
        return DataSourceBuilder.
                create()
                .username(dataSourceProperties.getUsername())
                .password(dataSourceProperties.getPassword())
                .url(dataSourceProperties.getUrl())
                .driverClassName(dataSourceProperties.getDriverClassName())
                .build();
    }

    @Primary
    @Bean("dataSourceProperties")
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() { return new DataSourceProperties(); }

    @Bean
    @Primary
    @ConfigurationProperties("spring.liquibase")
    LiquibaseProperties liquibaseProperties(){
        return new LiquibaseProperties() {
            @Override
            public String getPassword() { return super.getPassword();}
            @Override
            public  String getUser() { return super.getUser();}
        };
    }
}
