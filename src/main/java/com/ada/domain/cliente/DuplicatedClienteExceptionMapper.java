package com.ada.domain.cliente;

import com.ada.domain.cliente.dto.ErrorResponseDTO;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DuplicatedClienteExceptionMapper
        implements ExceptionMapper<DuplicatedClienteException> {

    @Override
    public Response toResponse(DuplicatedClienteException exception) {
        ErrorResponseDTO errorResponseDTO =
                new ErrorResponseDTO(exception.getCampo(), exception.getMessage());

        return Response
                .status(400)
                .entity(errorResponseDTO)
                .build();
    }
}
