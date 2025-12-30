package com.finance.payment.providers;

import com.finance.enums.PaymentProvider;
import com.finance.model.ProviderPaymentSession;
import com.finance.port.PaymentProviderClient;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class StripeClient implements PaymentProviderClient {
    @Value("${stripe.secret.key}")
    private String secretKey;

    @Override
    public ProviderPaymentSession initiatePayment(UUID paymentId, BigDecimal amount, String currency) {
        Stripe.apiKey = secretKey;

        long amountInCents = amount.multiply(BigDecimal.valueOf(100)).longValue();

        try {
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(amountInCents)
                            .setCurrency(currency.toLowerCase())
                            .putMetadata("paymentId", paymentId.toString())
                            .build();

            PaymentIntent intent = PaymentIntent.create(params);

            return new ProviderPaymentSession(
                    intent.getClientSecret(),
                    intent.getId(),
                    intent.getStatus()
            );

        } catch (StripeException e) {
            throw new IllegalStateException("Stripe initiation failed: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentProvider provider() {
        return PaymentProvider.STRIPE;
    }
}
