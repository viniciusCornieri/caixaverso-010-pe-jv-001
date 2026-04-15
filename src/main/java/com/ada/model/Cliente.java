package com.ada.model;

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

    public static void main(String[] args) {
        System.out.println(new Cliente("Vini", "123"));
    }

}
