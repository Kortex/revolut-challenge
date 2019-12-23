package com.ariskourt.revolut.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransferStrategyFactoryTest {

    private static final BigDecimal POSITIVE_AMOUNT = BigDecimal.valueOf(1000.0);
    private static final BigDecimal NEGATIVE_AMOUNT = BigDecimal.valueOf(-1000.0);

    private AmountTransferStrategy strategy;

    @Test
    @DisplayName("Verify correct implementation is returned in case the amount is positive")
    public void getStrategy_WhenAmountIsPositive_PositiveStrategyImplementationIsReturned() {
        strategy = TransferStrategyFactory.getStrategy(POSITIVE_AMOUNT);
        assertNotNull(strategy);
        assertThat(strategy, instanceOf(PositiveAmountStrategy.class));
    }

    @Test
    @DisplayName("Verify correct implementation is returned in case the amount is negative")
    public void getStrategy_WhenAmountIsNegative_NegativeStrategyImplementationIsReturned() {
        strategy = TransferStrategyFactory.getStrategy(NEGATIVE_AMOUNT);
        assertNotNull(strategy);
        assertThat(strategy, instanceOf(NegativeAmountStrategy.class));
    }

}