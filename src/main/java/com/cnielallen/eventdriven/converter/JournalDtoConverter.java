package com.cnielallen.eventdriven.converter;

import com.cnielallen.eventdriven.dto.JournalDto;
import com.cnielallen.eventdriven.entity.Journal;

import static java.util.Optional.ofNullable;

public class JournalDtoConverter {

    public JournalDto convert(Journal journal){
        return ofNullable(journal)
                .map(this::createJournalDtoWith)
                .orElse(null);
    }

    private JournalDto createJournalDtoWith(Journal journal){
        return JournalDto.builder()
                .journalCode(journal.getJournalCode())
                .journalName(journal.getJournalName())
                .description(journal.getDescription())
                .author(journal.getAuthor())
                .price(journal.getPrice())
                .createDate(journal.getCreateDate())
                .build();
    }

}
