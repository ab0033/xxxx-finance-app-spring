package com.finance.payment.event;

import event.PaymentSucceeded;
import com.finance.port.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringPaymentEventPublisher implements PaymentEventPublisher {
    private final ApplicationEventPublisher spring;

    @Override
    public void publish(PaymentSucceeded event) {
        spring.publishEvent(event);
    }
}
