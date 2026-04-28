package com.ada.domain.cliente;

import com.ada.domain.cliente.dto.ClienteResponseDTO;
import com.ada.domain.cliente.dto.ConsultaDeTaxaResponseDTO;
import com.ada.domain.cliente.dto.CriarClienteDTO;
import io.quarkus.security.Authenticated;
import io.quarkus.security.PermissionsAllowed;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.UUID;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class ClientesResource {

    @ConfigProperty(name = "taxas.valor-simulado")
    private Integer taxaSimulada;

    @Inject
    private ClientesService clientesService;

    @GET
    @RolesAllowed({"Gerente", "Admin"})
    public List<ClienteResponseDTO> consultaClientes() {
        return clientesService.listarClientes();
    }

    @POST
    public Response criarCliente(@Valid @NotNull CriarClienteDTO criarClienteDTO) {

        return Response
                .status(Response.Status.CREATED)
                .entity(clientesService.criarCliente(criarClienteDTO))
                .build();
    }

    @GET
    @Path("/{documento}")
    public Response consultarPorDocumento(@PathParam("documento") String documento) {

        return clientesService
                .consultarPorDocumento(documento)
                .map(cliente -> Response.ok(cliente).build())
                .orElse(Response.status(404).build());
    }

    @GET
    @Path("/{id}/taxas/{tipo_emprestimo}/elegibilidade")
    public ConsultaDeTaxaResponseDTO consultaDeTaxas(@PathParam("id") UUID idCliente,
                                                     @PathParam("tipo_emprestimo") TipoEmprestimo tipoEmprestimo) {

        return new ConsultaDeTaxaResponseDTO(idCliente, taxaSimulada);
    }

}
