package com.ariskourt.revolut.exceptions.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationError {

    ACCOUNT_NOT_FOUND (1),
    SAME_ACCOUNT_TRANSFER (2),
    INSUFFICIENT_ACCOUNT_BALANCE (3),
    DATA_ACCESS_ERROR(-1),
    INVALID_PAYLOAD_ERROR(-2),
    BAD_CURRENCY_API_CALL(-3),
    INTERNAL_SERVER_ERROR(-100);

    private final Integer code;

}
