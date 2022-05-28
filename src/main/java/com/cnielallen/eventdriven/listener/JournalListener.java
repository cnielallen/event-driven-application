package com.cnielallen.eventdriven.listener;

import com.cnielallen.eventdriven.converter.JournalConverter;
import com.cnielallen.eventdriven.converter.JournalDtoConverter;
import com.cnielallen.eventdriven.event.JournalEvent;
import com.cnielallen.eventdriven.exception.RetryableException;
import com.cnielallen.eventdriven.service.JournalService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JournalListener {

    private final JournalService journalService;
    private final JournalConverter journalConverter = new JournalConverter();


    @SneakyThrows
    public void onReceive(JournalEvent journalEvent){
        log.info("Receive Journal event message: {}", journalEvent);
        try {
            var journal = journalConverter.convert(journalEvent.getData());
            journalService.save(journal);
        } catch (Exception e) {
            log.error("Error occurred with exception {} database error, message put back on queue", e.getClass(), journalEvent);
            throw new RetryableException("Database error, message put back on queue", e);
        }

    }

}
