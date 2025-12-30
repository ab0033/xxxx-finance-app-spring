package com.finance.model;

import event.enums.LedgerDirection;
import event.enums.LedgerReason;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Builder(toBuilder = true)
@Getter
public class LedgerEntry {
    @NonNull
    private final LedgerEntryId id;
    @NonNull
    private final UserId userId;
    @NonNull
    private final Money money;
    @NonNull
    private final LedgerDirection direction;
    @NonNull
    private final LedgerReason reason;
    @NonNull
    private final UUID referenceId; // внешний reference: paymentId/refundId/chargebackId
    @NonNull
    private final Instant createdAt;

}
