package com.ada.domain.cliente;

import com.ada.domain.cep.dto.CEPResponseDTO;
import com.ada.domain.cep.dto.CEPRestClient;
import com.ada.domain.cliente.dto.ConsultaDeTaxaResponseDTO;
import com.ada.domain.cliente.dto.CriarClienteDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
public class ClientesResource {

    private final List<Cliente> clientes = new ArrayList<>();

    @Inject
    @RestClient
    private CEPRestClient cepRestClient;

    @ConfigProperty(name = "taxas.valor-simulado")
    private Integer taxaSimulada;

    @GET
    public List<Cliente> consultaClientes() {
        return clientes;
    }

    @POST
    public Response criarCliente(CriarClienteDTO criarClienteDTO) {

        CEPResponseDTO response = cepRestClient.getByCep(criarClienteDTO.cep());

        Endereco endereco = Endereco.builder()
                .cep(criarClienteDTO.cep())
                .numero(criarClienteDTO.numero())
                .complemento(criarClienteDTO.complemento())
                .logradouro(response.logradouro())
                .bairro(response.bairro())
                .build();

        Cliente novoCliente = new Cliente(
                criarClienteDTO.nome(),
                criarClienteDTO.documento(),
                endereco);
        clientes.add(novoCliente);
        return Response
                .status(Response.Status.CREATED)
                .entity(novoCliente)
                .build();
    }

    @GET
    @Path("/{documento}")
    public Response consultarPorDocumento(@PathParam("documento") String documento) {
        Optional<Response> clienteBuscado = clientes.stream()
                .filter(c -> c.getDocumento().equals(documento))
                .map(c -> Response.ok(c).build())
                .findFirst();

        return clienteBuscado
                .orElse(Response.status(404).build());
    }

    @GET
    @Path("/{id}/taxas/{tipo_emprestimo}/elegibilidade")
    public ConsultaDeTaxaResponseDTO consultaDeTaxas(@PathParam("id") UUID idCliente,
                                                     @PathParam("tipo_emprestimo") TipoEmprestimo tipoEmprestimo) {

        return new ConsultaDeTaxaResponseDTO(idCliente, taxaSimulada);
    }

}
