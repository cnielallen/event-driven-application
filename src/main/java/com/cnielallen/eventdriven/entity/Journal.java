package com.cnielallen.eventdriven.entity;

import liquibase.repackaged.net.sf.jsqlparser.statement.select.Offset;
import lombok.*;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Journal extends BaseEntity{
    private String journalName;
    private String description;
    private String author;
    private String journalCode;
    private BigDecimal price;
    private OffsetDateTime createDate;
}
