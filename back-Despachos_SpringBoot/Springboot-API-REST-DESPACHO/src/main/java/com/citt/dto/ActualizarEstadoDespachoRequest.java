package com.citt.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ActualizarEstadoDespachoRequest(
        @NotNull(message = "intento es obligatorio")
        @Min(value = 0, message = "intento no puede ser negativo")
        Integer intento,

        @NotNull(message = "despachado es obligatorio")
        Boolean despachado
) {
}
