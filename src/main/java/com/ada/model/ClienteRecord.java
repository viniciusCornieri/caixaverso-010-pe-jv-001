package com.ada.model;

import java.util.UUID;

public record ClienteRecord(UUID id, String nome, String documento) {

    public String nomeMaiusculo() {
        return nome.toUpperCase();
    }
}
