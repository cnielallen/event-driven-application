package com.cnielallen.eventdriven.publisher;


import com.cnielallen.eventdriven.dto.JournalDto;
import com.cnielallen.eventdriven.event.EventHeader;
import com.cnielallen.eventdriven.event.EventType;
import com.cnielallen.eventdriven.event.JournalEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JournalPublisher {

    private static final String JOURNAL_PUBLISHER_OUT_BINDING = "publishJournalDataToDownstreamTopic-out-0";
    private final StreamBridge streamBridge;

    public void publish(JournalDto journalDto, EventType eventType) {
        if(journalDto == null){
            log.warn("Unable to publish journal. Journal is null.");
            return;
        }

        log.info("Publishing journal [{}]", journalDto);
        streamBridge.send(JOURNAL_PUBLISHER_OUT_BINDING, createJournalEventWith(journalDto, eventType));
    }


    private JournalEvent createJournalEventWith(JournalDto journalDto, EventType eventType) {
        return JournalEvent.builder()
                .eventHeader(buildHeader(eventType))
                .data(journalDto)
                .build();
    }

    private EventHeader buildHeader(EventType eventType) {
        return EventHeader.builder()
                .eventType(eventType)
                .objectName("journal")
                .build();
    }
}
