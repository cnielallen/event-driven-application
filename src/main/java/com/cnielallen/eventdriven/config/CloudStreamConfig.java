package com.cnielallen.eventdriven.config;

import com.cnielallen.eventdriven.event.JournalEvent;
import com.cnielallen.eventdriven.listener.JournalListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class CloudStreamConfig {

    private final JournalListener journalListener;

    @Bean
    public Consumer<JournalEvent> processJournal() { return journalListener::onReceive;}
}
