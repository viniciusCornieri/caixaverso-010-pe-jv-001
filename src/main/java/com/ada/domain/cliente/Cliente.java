package com.ada.domain.cliente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    private final UUID id = UUID.randomUUID();
    private String nome;
    private String documento;
    private Endereco endereco;

}
