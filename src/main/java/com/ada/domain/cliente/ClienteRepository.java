package com.ada.domain.cliente;

import io.quarkus.hibernate.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class ClienteRepository implements PanacheRepository<Cliente> {

    public Optional<Cliente> findByDocumento(String documento) {
        return this.find("documento", documento)
                .firstResultOptional();
    }
}
