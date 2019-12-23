package com.ariskourt.revolut.services;

class TransferStrategyFactory {

    private TransferStrategyFactory() {}

    static AmountTransferStrategy getStrategy(Long amount) {
        return amount < 0 ? new NegativeAmountStrategy(amount) : new PositiveAmountStrategy(amount);
    }

}
