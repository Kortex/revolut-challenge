package com.ariskourt.revolut.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/latest")
@RegisterRestClient(configKey = "currency-api")
public interface CurrencyApiRestClient {

    @GET
    @Produces("application/json")
    CurrencyDetails getCurrencyDetails(@QueryParam("symbols") String symbols);

}
