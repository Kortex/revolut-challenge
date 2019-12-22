package com.ariskourt.revolut.domain;

import lombok.*;
import org.checkerframework.checker.units.qual.A;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BankAccount {

    private String id;
    private String accountHolder;
    private Long accountBalance;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;

    public void subFromBalance(Long amount) {
        var atomicBalance = new AtomicLong(accountBalance);
        accountBalance = atomicBalance.addAndGet(-amount);
    }

    public void addToBalance(Long amount) {
        var atomicBalance = new AtomicLong(accountBalance);
        accountBalance = atomicBalance.addAndGet(amount);
    }

}

