package com.finance.payment.controller;

import com.finance.payment.dto.InitiatePaymentRequest;
import com.finance.payment.dto.InitiatePaymentResponse;
import com.finance.payment.service.PaymentFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentFacade paymentFacade;

    @PostMapping("/top-up")
    public InitiatePaymentResponse initiatePayment(@Valid @NotNull @RequestBody InitiatePaymentRequest dto) {
        return paymentFacade.initiatePayment(dto);
    }
}
