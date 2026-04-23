package com.ada.domain.cliente;

import com.ada.domain.cep.dto.CEPResponseDTO;
import com.ada.domain.cep.dto.CEPRestClient;
import com.ada.domain.cliente.dto.ClienteResponseDTO;
import com.ada.domain.cliente.dto.CriarClienteDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ClientesService {

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    @RestClient
    private CEPRestClient cepRestClient;

    @Transactional
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
                UUID.randomUUID(),
                criarClienteDTO.nome(),
                criarClienteDTO.documento(),
                endereco);

        clienteRepository.persist(novoCliente);
        return ClienteResponseDTO.from(novoCliente);
    }

    public List<ClienteResponseDTO> listarClientes() {
        return clienteRepository
                .streamAll()
                .map(ClienteResponseDTO::from)
                .toList();
    }

    public Optional<ClienteResponseDTO> consultarPorDocumento(String documento) {
        return clienteRepository.find("documento", documento)
                .firstResultOptional()
                .map(ClienteResponseDTO::from);
    }
}
