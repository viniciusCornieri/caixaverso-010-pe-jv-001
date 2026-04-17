package com.ada.domain.cep.dto;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/ws")
@RegisterRestClient(configKey = "via-cep")
public interface CEPRestClient {

    @GET
    @Path("/{cep}/json")
    CEPResponseDTO getByCep(@PathParam("cep") String cep);

}
