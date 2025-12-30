package event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentDebited(
        UUID eventId,
        UUID userId,
        BigDecimal amount,
        String currency,
        UUID referenceId,   // paymentId / orderId
        Instant occurredAt
) {
}