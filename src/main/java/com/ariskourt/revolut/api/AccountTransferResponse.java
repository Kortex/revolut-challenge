package com.ariskourt.revolut.api;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransferResponse {

    private String fromAccount;
    private Long fromBalance;
    private String toAccount;
    private Long toBalance;
    private Long amount;
    private String message;

}
