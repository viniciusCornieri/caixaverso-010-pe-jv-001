package com.ada.domain.cliente;

import lombok.Getter;

@Getter
public class DuplicatedClienteException extends RuntimeException {

    private final String campo;

    public DuplicatedClienteException(String campo, String message) {
        super(message);
        this.campo = campo;
    }
}
