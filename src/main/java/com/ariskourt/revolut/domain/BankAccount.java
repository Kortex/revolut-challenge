package com.ariskourt.revolut.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BankAccount {

    private String id;
    private String accountHolder;
    private BigDecimal accountBalance;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;

    public void subFromBalance(BigDecimal amount) {
        accountBalance = accountBalance.subtract(amount);
    }

    public void addToBalance(BigDecimal amount) {
        accountBalance = accountBalance.add(amount);
    }

}

