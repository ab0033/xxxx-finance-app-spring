package com.finance.wallet.controller;

import com.finance.wallet.dto.WalletBalanceResponse;
import com.finance.wallet.service.WalletFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletFacade walletFacade;

    @GetMapping("/balance")
    public WalletBalanceResponse getBalance(
            @RequestParam UUID userId
    ) {
        return walletFacade.getBalance(userId);
    }
}
