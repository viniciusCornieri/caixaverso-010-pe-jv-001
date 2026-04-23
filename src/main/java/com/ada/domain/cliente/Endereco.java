package com.ada.domain.cliente;

import io.quarkus.hibernate.panache.PanacheEntity;
import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco extends PanacheEntity {

    private String cep;
    private String numero;
    private String complemento;
    private String logradouro;
    private String bairro;
}
