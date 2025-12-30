package com.finance.ledger.config;

import com.finance.port.LedgerEventPublisher;
import com.finance.port.LedgerRepository;
import com.finance.service.LedgerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class LedgerConfig {

    @Bean
    public LedgerService ledgerService(LedgerRepository ledgerRepository,
                                       LedgerEventPublisher eventPublisher) {
        return new LedgerService(ledgerRepository,
                eventPublisher,
                Clock.systemUTC());
    }
}
