package com.finance.ledger.mapper;

import com.finance.model.LedgerEntry;
import com.finance.model.LedgerEntryId;
import com.finance.model.Money;
import com.finance.model.UserId;
import com.finance.ledger.entity.LedgerEntryEntity;
import org.springframework.stereotype.Component;

@Component
public class LedgerEntryMapperImpl implements LedgerEntryMapper {

    public LedgerEntryEntity mapToEntity(LedgerEntry entry) {
        return LedgerEntryEntity.builder()
                .id(entry.getId().value())
                .userId(entry.getUserId().value())
                .amount(entry.getMoney().amount())
                .currency(entry.getMoney().currency())
                .direction(entry.getDirection())
                .reason(entry.getReason())
                .referenceId(entry.getReferenceId())
                .createdAt(entry.getCreatedAt())
                .build();
    }


    public LedgerEntry mapToDomain(LedgerEntryEntity e) {
        return LedgerEntry.builder()
                .id(new LedgerEntryId(e.getId()))
                .userId(new UserId(e.getUserId()))
                .money(Money.of(e.getAmount(), e.getCurrency()))
                .direction(e.getDirection())
                .reason(e.getReason())
                .referenceId(e.getReferenceId())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
