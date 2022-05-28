package com.cnielallen.eventdriven.service;

import com.cnielallen.eventdriven.converter.JournalDtoConverter;
import com.cnielallen.eventdriven.entity.Journal;
import com.cnielallen.eventdriven.event.EventType;
import com.cnielallen.eventdriven.publisher.JournalPublisher;
import com.cnielallen.eventdriven.repository.JournalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Slf4j
@Service
@RequiredArgsConstructor
public class JournalService {

    private static JournalDtoConverter journalDtoConverter = new JournalDtoConverter();
    private final JournalRepository journalRepository;
    private final JournalPublisher journalPublisher;

    public void save(Journal journal) {
        ofNullable(journal).ifPresentOrElse(newJournal -> {
            log.info("Saving Journal [Journal: {}]", newJournal);
            journalRepository.save(journal);
            log.info("Saved Journal [JournalCode: {}]", newJournal.getJournalCode());
            var journalDto = journalDtoConverter.convert(newJournal);
            journalPublisher.publish(journalDto, EventType.CREATE);
            } , () -> log.error("Received Journal is null"));
    }

    public Mono<Map<String, String>> publishJournals(){
        var journals = journalRepository.findAll();
        log.info("Publishing {} journals:", journals.size());
        journals.forEach(journal -> journalPublisher.publish(journalDtoConverter.convert(journal), EventType.CREATE));
        return Mono.just(Collections.singletonMap("response", "Journals published successfully"));
    }

}
