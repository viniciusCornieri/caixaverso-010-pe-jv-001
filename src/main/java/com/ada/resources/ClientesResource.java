package com.ada.resources;

import com.ada.model.Cliente;
import com.ada.views.CriarClienteDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
public class ClientesResource {

    private final List<Cliente> clientes = new ArrayList<>();

    @GET
    public List<Cliente> consultaClientes() {
        return clientes;
    }

    @POST
    public Response criarCliente(CriarClienteDTO criarClienteDTO) {
        Cliente novoCliente = new Cliente(criarClienteDTO.nome(), criarClienteDTO.documento());
        clientes.add(novoCliente);
        return Response
                .status(Response.Status.CREATED)
                .entity(novoCliente)
                .build();
    }

    @GET
    @Path("/documento/{id}")
    public Response consultarPorDocumento(@PathParam("id") String documento) {
        Optional<Response> clienteBuscado = clientes.stream()
                .filter(c -> c.getDocumento().equals(documento))
                .map(c -> Response.ok(c).build())
                .findFirst();

        return clienteBuscado
                .orElse(Response.noContent().build());
    }

}
