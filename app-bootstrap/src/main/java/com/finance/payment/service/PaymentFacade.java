package com.finance.payment.service;

import com.finance.enums.PaymentStatus;
import com.finance.model.Payment;
import com.finance.model.ProviderPaymentSession;
import com.finance.payment.dto.InitiatePaymentRequest;
import com.finance.payment.dto.InitiatePaymentResponse;
import com.finance.payment.registry.PaymentProviderClientRegistry;
import com.finance.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentService paymentService;
    private final PaymentProviderClientRegistry providerRegistry;

    @Transactional
    public InitiatePaymentResponse initiatePayment(InitiatePaymentRequest dto) {
        Payment payment = paymentService.initiatePayment(
                dto.getUserId(),
                dto.getProvider(),
                dto.getCurrency(),
                dto.getAmount(),
                dto.getIdempotencyKey());

        if (payment.getStatus() != PaymentStatus.INITIATED) {
            return InitiatePaymentResponse.builder()
                    .status(payment.getStatus())
                    .paymentId(payment.getId())
                    .currency(payment.getCurrency())
                    .amount(payment.getAmount().toPlainString())
                    .provider(payment.getProvider())
                    .providerPaymentId(payment.getProviderPaymentId())
                    .providerSecret(null)
                    .build();
        }

        ProviderPaymentSession session = providerRegistry
                .get(dto.getProvider())
                .initiatePayment(payment.getId(), dto.getAmount(), dto.getCurrency());

        Payment created = paymentService.markCreated(payment, session.providerPaymentId(), session.status());

        return InitiatePaymentResponse.builder()
                .status(created.getStatus())
                .paymentId(created.getId())
                .currency(created.getCurrency())
                .amount(created.getAmount().toPlainString())
                .provider(created.getProvider())
                .providerPaymentId(session.providerPaymentId())
                .providerSecret(session.providerSecret())
                .build();
    }

    @Transactional
    public Payment markSucceeded(UUID paymentId, String providerPaymentStatus) {
        return paymentService.markSucceeded(paymentId, providerPaymentStatus);
    }

    @Transactional
    public Payment markFailed(UUID paymentId, String providerPaymentStatus) {
        return paymentService.markFailed(paymentId, providerPaymentStatus);
    }
}
