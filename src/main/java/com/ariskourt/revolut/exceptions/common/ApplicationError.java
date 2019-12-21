package com.ariskourt.revolut.exceptions.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationError {

    ACCOUNT_NOT_FOUND (1),
    SAME_ACCOUNT_TRANSFER (2),
    INSUFFICIENT_ACCOUNT_BALANCE (3);

    private final Integer errorCode;

}
