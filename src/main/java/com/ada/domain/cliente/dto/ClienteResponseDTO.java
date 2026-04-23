package com.ada.domain.cliente.dto;

import com.ada.domain.cliente.Cliente;
import com.ada.domain.cliente.Endereco;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ClienteResponseDTO(

        UUID id,
        String nome,
        String documento,
        EnderecoResponseDTO endereco
) {

    public static ClienteResponseDTO from(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .documento(cliente.getDocumento())
                .nome(cliente.getNome())
                .endereco(EnderecoResponseDTO.from(cliente.getEndereco()))
                .build();
    }
}
