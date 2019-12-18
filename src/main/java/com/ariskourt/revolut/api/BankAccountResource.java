package com.ariskourt.revolut.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BankAccountResource {

    private String holder;
    private Integer balance;

}
