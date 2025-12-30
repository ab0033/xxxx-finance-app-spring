package com.finance.payment.controller;

import com.finance.payment.handlers.StripeWebhookHandler;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/webhook/stripe")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;
    private final StripeWebhookHandler stripeWebhookHandler;

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(HttpServletRequest request) throws IOException {
        String payload = new String(
                request.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        String sigHeader = request.getHeader("Stripe-Signature");

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            log.error("Stripe signature verification failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        stripeWebhookHandler.handle(event);

        return ResponseEntity.ok("");
    }
}
