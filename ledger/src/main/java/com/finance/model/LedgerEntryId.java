package com.finance.model;

import java.util.UUID;

public record LedgerEntryId(UUID value) {
    public LedgerEntryId {
        if (value == null) {
            throw new IllegalArgumentException("Value is null");
        }
    }

    public static LedgerEntryId newId() {
        return new LedgerEntryId(UUID.randomUUID());
    }
}
