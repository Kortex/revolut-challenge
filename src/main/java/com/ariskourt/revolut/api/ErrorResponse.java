package com.ariskourt.revolut.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {

    private final Integer errorCode;
    private final String message;
    private String stacktrace;

}
