package com.finance.service;

import com.finance.model.LedgerEntry;
import com.finance.model.LedgerEntryId;
import com.finance.model.Money;
import com.finance.model.UserId;
import com.finance.port.LedgerEventPublisher;
import com.finance.port.LedgerRepository;
import event.LedgerEntryCreated;
import event.enums.LedgerDirection;
import event.enums.LedgerReason;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public final class LedgerService {
    @NonNull
    private final LedgerRepository repository;
    @NonNull
    private final LedgerEventPublisher publisher;
    @NonNull
    private final Clock clock;

    public LedgerEntry applyTopUp(UserId userId, Money money, UUID referenceId) {
        return apply(userId, money, LedgerDirection.CREDIT, LedgerReason.TOP_UP, referenceId);
    }

    public LedgerEntry applyRefund(UserId userId, Money money, UUID referenceId) {
        return apply(userId, money, LedgerDirection.DEBIT, LedgerReason.REFUND, referenceId);
    }

    private LedgerEntry apply(
            @NonNull UserId userId,
            @NonNull Money money,
            @NonNull LedgerDirection direction,
            @NonNull LedgerReason reason,
            @NonNull UUID referenceId
    ) {
        if (repository.existsByReferenceIdAndReason(referenceId, reason)) {
            return repository.findByReferenceIdAndReason(referenceId, reason)
                    .orElseThrow(() -> new IllegalStateException("Ledger already applied but entry not found"));
        }

        Instant now = Instant.now(clock);
        LedgerEntry entry = new LedgerEntry(
                LedgerEntryId.newId(),
                userId,
                money,
                direction,
                reason,
                referenceId,
                now
        );

        LedgerEntry saved = repository.save(entry);

        publisher.publish(new LedgerEntryCreated(
                UUID.randomUUID(),
                saved.getId().value(),
                saved.getUserId().value(),
                saved.getMoney().amount(),
                saved.getMoney().currency(),
                saved.getDirection(),
                saved.getReason(),
                saved.getReferenceId(),
                saved.getCreatedAt()
        ));

        return saved;
    }
}
