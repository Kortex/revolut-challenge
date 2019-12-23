package com.ariskourt.revolut.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccountTransferRequest {

    @NotNull(message = "A fromAccount ID must be provided")
    private String fromAccount;

    @NotNull(message = "A toAccount ID must be provided")
    private String toAccount;

    @NotNull(message = "A transfer amount must be provided")
    private BigDecimal amount;

}
