package com.ariskourt.revolut.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BankAccount {

    private String id;
    private String accountHolder;
    private Long accountBalance;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;

}

