package com.ariskourt.revolut.api;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferResponse {

    private String fromAccount;
    private BigDecimal fromBalance;
    private String toAccount;
    private BigDecimal toBalance;
    private BigDecimal amount;
    private String message;

}
