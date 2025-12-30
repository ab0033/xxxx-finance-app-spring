package event;

import event.enums.LedgerDirection;
import event.enums.LedgerReason;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LedgerEntryCreated(
        UUID eventId,
        UUID ledgerEntryId,
        UUID userId,
        BigDecimal amount,
        String currency,
        LedgerDirection direction,
        LedgerReason reason,
        UUID referenceId,
        Instant occurredAt
) {
}
