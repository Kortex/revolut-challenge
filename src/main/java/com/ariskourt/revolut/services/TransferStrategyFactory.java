package com.ariskourt.revolut.services;

import java.math.BigDecimal;

class TransferStrategyFactory {

    private TransferStrategyFactory() {}

    static AmountTransferStrategy getStrategy(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0 ? new NegativeAmountStrategy(amount) : new PositiveAmountStrategy(amount);
    }

}
