package com.ariskourt.revolut.exceptions.common;

import lombok.Getter;

import javax.ws.rs.core.Response;

import java.text.MessageFormat;

public abstract class AbstractApplicationException extends RuntimeException {

    @Getter
    private final Integer errorCode;

    public AbstractApplicationException(String message, ApplicationError error) {
        super(message);
        this.errorCode = error.getCode();
    }

    public AbstractApplicationException(String message, ApplicationError error, Object... args) {
        super(MessageFormat.format(message, args));
        this.errorCode = error.getCode();
    }

    public abstract Response.Status getStatus();

}
