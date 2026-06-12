package com.server.app.dto.finanzas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MovimientoCreateDto {

    @NotNull(message = "El monto no puede ser nulo")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto;

    @NotBlank(message = "La moneda original no puede estar vacía")
    private String monedaOriginal;

    @NotNull(message = "La tasa de cambio no puede ser nula")
    @Positive(message = "La tasa de cambio debe ser mayor a 0")
    private Double tasaCambio;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "El ID de cuenta no puede ser nulo")
    private Integer cuentaId;

    @NotNull(message = "El ID de categoría no puede ser nulo")
    private Integer categoriaId;
}