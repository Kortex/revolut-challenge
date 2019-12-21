package com.ariskourt.revolut.exceptions;

import com.ariskourt.revolut.exceptions.common.AbstractApplicationException;
import com.ariskourt.revolut.exceptions.common.ApplicationError;

import javax.ws.rs.core.Response;

import java.sql.SQLException;

public class DataAccessException extends AbstractApplicationException {

    public DataAccessException(SQLException e) {
        super(e.getMessage(), ApplicationError.DATA_ACCESS_ERROR);
    }

    @Override
    public Response.Status getStatus() {
	return Response.Status.INTERNAL_SERVER_ERROR;
    }

}
