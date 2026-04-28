package com.ada.domain.cliente;

import com.ada.domain.cliente.dto.ClienteResponseDTO;
import com.ada.domain.cliente.dto.CriarClienteDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.restassured.RestAssured.given;

@QuarkusTest
class ClientesResourceTest {

    public static final String DOCUMENTO_CLIENTE = "12345678901";

    @Inject
    ClienteRepository clienteRepository;

    @AfterEach
    @Transactional
    void tearDown() {
        clienteRepository.delete("documento", DOCUMENTO_CLIENTE);
    }

    @ParameterizedTest
    @CsvSource({
        "Vini",
        "ViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniViniVi"
    })
    void devoCriarComSucessoUmCliente(String nome) {

        CriarClienteDTO request = CriarClienteDTO.builder()
                .nome(nome)
                .documento(DOCUMENTO_CLIENTE)
                .cep("01001000")
                .numero("12345")
                .build();

        ClienteResponseDTO response =
                given()
                .body(request)
                .header("Content-type",  "application/json")
                .when().post("/clientes")
                .then()
                .statusCode(201)
                .extract().body().as(ClienteResponseDTO.class);

        Assertions.assertEquals(request.nome(), response.nome());
        Assertions.assertEquals(request.documento(), response.documento());
        Assertions.assertNotNull(response.id());
    }

    @Test
    void deveRejeitarCriacaoDoClienteComNomeMenorQue4() {

        CriarClienteDTO request = CriarClienteDTO.builder()
                .nome("Vin")
                .documento("12345678901")
                .cep("01001000")
                .numero("12345")
                .build();

                given()
                        .body(request)
                        .header("Content-type",  "application/json")
                        .when().post("/clientes")
                        .then()
                        .statusCode(400)
                        .body(Matchers.containsString("tamanho deve ser entre 4 e 150"));

    }
}