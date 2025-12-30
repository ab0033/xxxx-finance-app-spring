package com.finance.ledger.mapper;

import com.finance.model.LedgerEntry;
import com.finance.ledger.entity.LedgerEntryEntity;

public interface LedgerEntryMapper {
    LedgerEntryEntity mapToEntity(LedgerEntry entry);

    LedgerEntry mapToDomain(LedgerEntryEntity e);
}
