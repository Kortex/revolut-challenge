package com.ariskourt.revolut.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccountTransferResponse {

    private String fromAccount;
    private Long fromBalance;
    private String toAccount;
    private Long toBalance;
    private Long amount;
    private String message;

}
