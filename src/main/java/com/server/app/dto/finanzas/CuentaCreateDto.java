package com.server.app.dto.finanzas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CuentaCreateDto {

    @NotBlank(message = "El alias no puede estar vacío")
    private String alias;

    @NotBlank(message = "La moneda no puede estar vacía")
    private String moneda;

    @NotNull(message = "El saldo base no puede ser nulo")
    @Positive(message = "El saldo base debe ser mayor a 0")
    private Double saldoBase;

    @NotBlank(message = "El tipo no puede estar vacío")
    private String tipo; // Ahorro o Corriente
}