package com.ada.domain.cliente;

import com.ada.domain.cep.dto.CEPResponseDTO;
import com.ada.domain.cep.dto.CEPRestClient;
import com.ada.domain.cliente.dto.ClienteResponseDTO;
import com.ada.domain.cliente.dto.CriarClienteDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClientesService {

    private final List<Cliente> clientes = new ArrayList<>();

    @Inject
    @RestClient
    private CEPRestClient cepRestClient;

    public ClienteResponseDTO criarCliente(CriarClienteDTO criarClienteDTO) {
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
        return ClienteResponseDTO.from(novoCliente);
    }

    public List<ClienteResponseDTO> listarClientes() {
        return clientes
                .stream()
                .map(ClienteResponseDTO::from)
                .toList();
    }

    public Optional<ClienteResponseDTO> consultarPorDocumento(String documento) {
        return clientes.stream()
                .filter(c -> c.getDocumento().equals(documento))
                .map(ClienteResponseDTO::from)
                .findFirst();
    }
}
