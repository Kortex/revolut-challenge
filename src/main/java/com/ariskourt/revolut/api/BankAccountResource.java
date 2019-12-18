package com.ariskourt.revolut.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BankAccountResource {

    private String holder;
    private Integer balance;

}
