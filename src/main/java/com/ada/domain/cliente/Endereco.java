package com.ada.domain.cliente;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Endereco {

    private String cep;
    private String numero;
    private String complemento;
    private String logradouro;
    private String bairro;
}
