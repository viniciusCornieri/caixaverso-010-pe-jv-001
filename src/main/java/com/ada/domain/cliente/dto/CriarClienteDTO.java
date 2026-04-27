package com.ada.domain.cliente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CriarClienteDTO(
        @NotBlank(message = "O campo nome é obrigatório") @Size(min = 4, max = 15)
        String nome,
        @NotBlank(message = "O campo documento é obrigatório") @Pattern(regexp = "^[0-9]{11}$|^[0-9A-Z]{12}[0-9]{2}$")
        String documento,
        @NotBlank(message = "O campo cep é obrigatório") @Pattern(regexp = "^[0-9]{8}$")
        String cep,
        @NotBlank(message = "O campo numero é obrigatório") @Size(min = 1, max = 20)
        String numero,
        @Size(min = 1, max = 50)
        String complemento
) {
}
