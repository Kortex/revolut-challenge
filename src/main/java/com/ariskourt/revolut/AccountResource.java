package com.ariskourt.revolut;

import com.ariskourt.revolut.api.AccountTransferRequest;
import com.ariskourt.revolut.services.AccountQueryService;
import com.ariskourt.revolut.services.AccountTransferService;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class AccountResource {

    private final AccountTransferService transferService;
    private final AccountQueryService queryService;

    @POST
    @Path("/transfer")
    public Response transfer(@Valid AccountTransferRequest resource) {
        return Response.status(Response.Status.OK)
            .entity(transferService.transferAmount(resource))
            .build();
    }

    @GET
    public Response list() {
        return Response.status(Response.Status.OK)
            .entity(queryService.getAccounts())
            .build();
    }

}