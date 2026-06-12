package com.server.app.dto.finanzas;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransferenciaDto {

    @NotNull(message = "La cuenta origen no puede ser nula")
    private Integer cuentaOrigenId;

    @NotNull(message = "La cuenta destino no puede ser nula")
    private Integer cuentaDestinoId;

    @NotNull(message = "El monto no puede ser nulo")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto;

    private String descripcion;
}