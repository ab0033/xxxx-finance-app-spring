package com.finance.ledger.repository;

import event.enums.LedgerReason;
import com.finance.ledger.entity.LedgerEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringLedgerJpaRepository extends JpaRepository<LedgerEntryEntity, UUID> {

    boolean existsByReferenceIdAndReason(UUID referenceId, LedgerReason reason);

    Optional<LedgerEntryEntity> findByReferenceIdAndReason(
            UUID referenceId,
            LedgerReason reason
    );
}
