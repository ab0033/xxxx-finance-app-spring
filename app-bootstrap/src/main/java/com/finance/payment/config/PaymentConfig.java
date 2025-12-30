package com.finance.payment.config;

import com.finance.port.PaymentEventPublisher;
import com.finance.port.PaymentRepository;
import com.finance.service.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class PaymentConfig {
    @Bean
    public PaymentService paymentService(
            PaymentRepository repository,
            PaymentEventPublisher publisher
    ) {
        return new PaymentService(
                repository,
                publisher,
                Clock.systemUTC()
        );
    }
}
