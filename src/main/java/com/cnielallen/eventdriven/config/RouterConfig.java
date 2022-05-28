package com.cnielallen.eventdriven.config;

import com.cnielallen.eventdriven.handler.JournalHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse>  routes(JournalHandler journalHandler) {
        return RouterFunctions
                .route(POST("/publish/journals").and(accept(APPLICATION_JSON)),
                        journalHandler::publishJournals);
    }

}
