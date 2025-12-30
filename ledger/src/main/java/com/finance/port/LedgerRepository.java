package com.finance.port;

import com.finance.model.LedgerEntry;
import event.enums.LedgerReason;

import java.util.Optional;
import java.util.UUID;

public interface LedgerRepository {
    /** Idempotence check */
    boolean existsByReferenceIdAndReason(UUID referenceId, LedgerReason reason);

    LedgerEntry save(LedgerEntry entry);

    Optional<LedgerEntry> findByReferenceIdAndReason(UUID referenceId, LedgerReason reason);
}
