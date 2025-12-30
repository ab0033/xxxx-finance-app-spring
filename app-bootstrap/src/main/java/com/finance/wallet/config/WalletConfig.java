package com.finance.wallet.config;

import com.finance.port.WalletRepository;
import com.finance.service.WalletService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WalletConfig {

    @Bean
    public WalletService walletService(WalletRepository walletRepository) {
        return new WalletService(walletRepository);
    }
}
