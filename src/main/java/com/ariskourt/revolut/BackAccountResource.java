package com.ariskourt.revolut;

import com.ariskourt.revolut.api.AccountTransferRequest;
import com.ariskourt.revolut.services.AccountTransferService;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class BackAccountResource {

    private final AccountTransferService transferService;

    @POST
    @Path("/transfer")
    public Response transfer(AccountTransferRequest resource) {
        return Response.status(Response.Status.OK)
            .entity(transferService.transferAmount(resource))
            .build();
    }

}