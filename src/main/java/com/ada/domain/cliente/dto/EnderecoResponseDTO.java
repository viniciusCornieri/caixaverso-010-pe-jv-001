package com.ada.domain.cliente.dto;

import com.ada.domain.cliente.Endereco;
import lombok.Builder;

@Builder
public record EnderecoResponseDTO(
        String cep,
        String numero,
        String complemento,
        String logradouro,
        String bairro
) {

    public static EnderecoResponseDTO from(Endereco endereco) {
        return EnderecoResponseDTO
                .builder()
                .cep(endereco.getCep())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .logradouro(endereco.getLogradouro())
                .bairro(endereco.getBairro())
                .build();
    }
}
