package com.cnielallen.eventdriven.converter;

import com.cnielallen.eventdriven.dto.JournalDto;
import com.cnielallen.eventdriven.entity.Journal;

import static java.util.Optional.ofNullable;

public class JournalConverter {

    public Journal convert(JournalDto journalDto){
        return ofNullable(journalDto)
                .map(this::createJournalWith)
                .orElse(null);
    }

    private Journal createJournalWith(JournalDto journalDto){
        return Journal.builder()
                .journalCode(journalDto.getJournalCode())
                .journalName(journalDto.getJournalName())
                .description(journalDto.getDescription())
                .author(journalDto.getAuthor())
                .price(journalDto.getPrice())
                .createDate(journalDto.getCreateDate())
                .build();
    }

}
