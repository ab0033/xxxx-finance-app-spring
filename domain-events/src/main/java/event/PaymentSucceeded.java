package event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentSucceeded(
        UUID eventId,
        UUID paymentId,
        UUID userId,
        BigDecimal amount,
        String currency,
        UUID referenceId,
        Instant occurredAt
) {
}
