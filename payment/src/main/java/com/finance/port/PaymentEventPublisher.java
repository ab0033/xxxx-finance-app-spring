package com.finance.port;


import event.PaymentSucceeded;

@FunctionalInterface
public interface PaymentEventPublisher {
    void publish(PaymentSucceeded payment);
}
