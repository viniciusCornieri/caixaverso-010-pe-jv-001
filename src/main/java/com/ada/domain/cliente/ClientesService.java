package com.ada.domain.cliente;

import com.ada.domain.cep.dto.CEPResponseDTO;
import com.ada.domain.cep.dto.CEPRestClient;
import com.ada.domain.cliente.dto.ClienteResponseDTO;
import com.ada.domain.cliente.dto.CriarClienteDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class ClientesService {

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    @RestClient
    private CEPRestClient cepRestClient;

    @Transactional
    @Retry(maxRetries = 1, delay = 100)
    public ClienteResponseDTO criarCliente(CriarClienteDTO criarClienteDTO) {
        if (clienteRepository.count("documento", criarClienteDTO.documento()) > 0) {
            throw new DuplicatedClienteException("documento", "Já existe um cliente com este documento");
        }

        log.info("Criando cliente XPTO");

        Endereco endereco = criarEndereco(criarClienteDTO);

        Cliente novoCliente = new Cliente(
                UUID.randomUUID(),
                criarClienteDTO.nome(),
                criarClienteDTO.documento(),
                endereco);

        clienteRepository.persistAndFlush(novoCliente);
        log.debug("Cliente criado com sucesso");

        return ClienteResponseDTO.from(novoCliente);
    }

    private Endereco criarEndereco(CriarClienteDTO criarClienteDTO) {
        CEPResponseDTO response = cepRestClient.getByCep(criarClienteDTO.cep());

        return Endereco.builder()
                .cep(criarClienteDTO.cep())
                .numero(criarClienteDTO.numero())
                .complemento(criarClienteDTO.complemento())
                .logradouro(response.logradouro())
                .bairro(response.bairro())
                .build();
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
