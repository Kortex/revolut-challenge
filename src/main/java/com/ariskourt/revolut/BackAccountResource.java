package com.ariskourt.revolut;

import com.ariskourt.revolut.api.BankAccountResource;
import com.ariskourt.revolut.domain.BankAccount;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/accounts")
@Slf4j
public class BackAccountResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/create")
    @Transactional
    public String create(BankAccountResource resource) {
        log.info("About to persist the following account:: {}", resource);
        BankAccount account = new BankAccount();
        account.setAccountBalance(resource.getBalance());
        account.setAccountHolder(resource.getHolder());
        account.persist();
        return BankAccount.listAll().get(0).toString();
    }

}