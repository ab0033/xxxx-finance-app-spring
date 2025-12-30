package com.finance.ledger.adapter;

import com.finance.model.LedgerEntry;
import com.finance.port.LedgerRepository;
import event.enums.LedgerReason;
import com.finance.ledger.entity.LedgerEntryEntity;
import com.finance.ledger.mapper.LedgerEntryMapper;
import com.finance.ledger.repository.SpringLedgerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaLedgerRepository implements LedgerRepository {
    private final SpringLedgerJpaRepository jpa;
    private final LedgerEntryMapper mapper;

    @Override
    public boolean existsByReferenceIdAndReason(UUID referenceId, LedgerReason reason) {
        return jpa.existsByReferenceIdAndReason(referenceId, reason);
    }

    @Override
    public LedgerEntry save(LedgerEntry entry) {
        LedgerEntryEntity entity = mapper.mapToEntity(entry);
        jpa.save(entity);
        return mapper.mapToDomain(entity);
    }

    @Override
    public Optional<LedgerEntry> findByReferenceIdAndReason(UUID referenceId, LedgerReason reason) {
        return jpa.findByReferenceIdAndReason(referenceId, reason).map(mapper::mapToDomain);
    }
}
