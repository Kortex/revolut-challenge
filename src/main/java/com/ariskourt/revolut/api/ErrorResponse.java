package com.ariskourt.revolut.api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Integer errorCode;
    private String message;
    private String stacktrace;

}
