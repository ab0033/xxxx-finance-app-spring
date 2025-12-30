package com.finance.payment.handlers;

import com.finance.payment.service.PaymentFacade;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookHandler {
    private static final String PI_CREATED   = "payment_intent.created";
    private static final String PI_SUCCEEDED = "payment_intent.succeeded";
    private static final String PI_FAILED    = "payment_intent.payment_failed";

    private final PaymentFacade paymentFacade;

    public void handle(Event event) {
        final String type = event.getType();
        final String eventId = event.getId();

        switch (type) {
            case PI_SUCCEEDED -> onSucceeded(eventId, type, event);
            case PI_FAILED -> onFailed(eventId, type, event);
            case PI_CREATED -> log.info("stripe.webhook_received_ignored type={} event_id={} livemode={}", type, eventId, event.getLivemode());
            default -> log.info("stripe.webhook_received_unknown type={} event_id={} livemode={}", type, eventId, event.getLivemode());
        }
    }

    private void onSucceeded(String eventId, String type, Event event) {
        PaymentIntent intent = extractPaymentIntent(event).orElse(null);
        if (intent == null) {
            log.error("stripe.payload_invalid reason=no_payment_intent event_id={} type={}", eventId, type);
            return;
        }

        final String providerPaymentId = intent.getId();
        final String paymentIdStr = metadataPaymentId(intent);

        log.info("stripe.pi_succeeded_received event_id={} type={} provider_payment_id={} payment_id={} livemode={}",
                eventId, type, providerPaymentId, paymentIdStr, event.getLivemode());

        if (paymentIdStr == null) {
            log.warn("stripe.pi_succeeded_missing_payment_id event_id={} type={} provider_payment_id={}",
                    eventId, type, providerPaymentId);
            return;
        }

        try {
            UUID paymentId = UUID.fromString(paymentIdStr);
            paymentFacade.markSucceeded(paymentId, type);
            log.info("stripe.payment_marked_succeeded event_id={} payment_id={} provider_status={}",
                    eventId, paymentId, type);
        } catch (IllegalArgumentException badUuid) {
            log.error("stripe.pi_succeeded_invalid_payment_id event_id={} payment_id_str={} type={}",
                    eventId, paymentIdStr, type, badUuid);
        } catch (Exception ex) {
            log.error("stripe.payment_mark_succeeded_failed event_id={} payment_id={} provider_status={}",
                    eventId, paymentIdStr, type, ex);
            throw ex;
        }
    }

    private void onFailed(String eventId, String type, Event event) {
        PaymentIntent intent = extractPaymentIntent(event).orElse(null);
        if (intent == null) {
            log.error("stripe.payload_invalid reason=no_payment_intent event_id={} type={}", eventId, type);
            return;
        }

        final String providerPaymentId = intent.getId();
        final String paymentIdStr = metadataPaymentId(intent);
        final var lastError = intent.getLastPaymentError();
        final String failureCode = lastError != null ? lastError.getCode() : null;
        final String failureMessage = lastError != null ? lastError.getMessage() : null;

        log.info("stripe.pi_failed_received event_id={} type={} provider_payment_id={} payment_id={} failure_code={} failure_message={} livemode={}",
                eventId, type, providerPaymentId, paymentIdStr, failureCode, failureMessage, event.getLivemode());

        if (paymentIdStr == null) {
            log.warn("stripe.pi_failed_missing_payment_id event_id={} type={} provider_payment_id={}",
                    eventId, type, providerPaymentId);
            return;
        }

        try {
            UUID paymentId = UUID.fromString(paymentIdStr);
            paymentFacade.markFailed(paymentId, type);
            log.info("stripe.payment_marked_failed event_id={} payment_id={} provider_status={}",
                    eventId, paymentId, type);
        } catch (IllegalArgumentException badUuid) {
            log.error("stripe.pi_failed_invalid_payment_id event_id={} payment_id_str={} type={}",
                    eventId, paymentIdStr, type, badUuid);
        } catch (Exception ex) {
            log.error("stripe.payment_mark_failed_failed event_id={} payment_id={} provider_status={}",
                    eventId, paymentIdStr, type, ex);
            throw ex;
        }
    }

    private Optional<PaymentIntent> extractPaymentIntent(Event event) {
        return event.getDataObjectDeserializer()
                .getObject()
                .filter(PaymentIntent.class::isInstance)
                .map(PaymentIntent.class::cast);
    }

    private static String metadataPaymentId(PaymentIntent intent) {
        return intent.getMetadata() != null ? intent.getMetadata().get("paymentId") : null;
    }
}
