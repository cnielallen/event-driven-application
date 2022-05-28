package com.cnielallen.eventdriven.handler;


import com.cnielallen.eventdriven.service.JournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JournalHandler {

    private final JournalService journalService;

    public Mono<ServerResponse> publishJournals(ServerRequest serverRequest){
        return ServerResponse.ok().body(journalService.publishJournals(), Map.class);

    }
}
