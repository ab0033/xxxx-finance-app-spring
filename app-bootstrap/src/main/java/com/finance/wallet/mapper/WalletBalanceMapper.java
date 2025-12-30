package com.finance.wallet.mapper;

import com.finance.model.WalletBalance;
import com.finance.wallet.entity.WalletBalanceEntity;

public interface WalletBalanceMapper {
    WalletBalance toDomain(WalletBalanceEntity entity);

    WalletBalanceEntity toEntity(WalletBalance balance);
}
