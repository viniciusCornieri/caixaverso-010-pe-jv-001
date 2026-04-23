package com.ada.domain.cliente;

import io.quarkus.hibernate.panache.PanacheEntity;
import io.quarkus.hibernate.panache.PanacheEntityBase;
import io.quarkus.hibernate.panache.PanacheEntityMarker;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente extends PanacheEntityBase {

    @Id
    private UUID id;

    private String nome;

    private String documento;

    @OneToOne(cascade = CascadeType.ALL)
    private Endereco endereco;

}
