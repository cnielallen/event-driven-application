package com.cnielallen.eventdriven.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JournalDto {
    @NotBlank
    @Size(
            max = 10
    )
    private String journalCode;
    @NotBlank
    private String description;
    @NotBlank
    private String journalName;
    @NotBlank
    private String author;
    private BigDecimal price;
    private OffsetDateTime createDate;
}
