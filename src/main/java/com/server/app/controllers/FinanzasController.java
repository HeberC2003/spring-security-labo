package com.server.app.controllers;

import com.server.app.dto.finanzas.CuentaCreateDto;
import com.server.app.dto.finanzas.MovimientoCreateDto;
import com.server.app.dto.finanzas.TransferenciaDto;
import com.server.app.dto.response.Pagination;
import com.server.app.dto.response.PaginationMeta;
import com.server.app.entities.Categoria;
import com.server.app.entities.Cuenta;
import com.server.app.entities.Movimiento;
import com.server.app.entities.User;
import com.server.app.services.FinanzasService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/finanzas")
@AllArgsConstructor
public class FinanzasController {

    private final FinanzasService finanzasService;

    @GetMapping("/cuentas")
    public ResponseEntity<?> getCuentas(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Cuenta> result = finanzasService.getCuentas(user, page - 1, size);
        return ResponseEntity.ok(new Pagination<>(
                result.getContent(),
                new PaginationMeta(page, size, result.getTotalPages(), result.getTotalElements())
        ));
    }

    @PostMapping("/cuentas")
    public ResponseEntity<?> createCuenta(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CuentaCreateDto dto) {
        return ResponseEntity.ok(finanzasService.createCuenta(user, dto));
    }

    @GetMapping("/movimientos")
    public ResponseEntity<?> getMovimientos(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        Page<Movimiento> result = finanzasService.getMovimientos(user, page - 1, size, fechaInicio, fechaFin);
        return ResponseEntity.ok(new Pagination<>(
                result.getContent(),
                new PaginationMeta(page, size, result.getTotalPages(), result.getTotalElements())
        ));
    }

    @PostMapping("/transferencias")
    public ResponseEntity<?> transferir(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody TransferenciaDto dto) {
        return ResponseEntity.ok(finanzasService.transferir(user, dto));
    }

    @GetMapping("/categorias")
    public ResponseEntity<?> getCategorias(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Categoria> result = finanzasService.getCategorias(page - 1, size);
        return ResponseEntity.ok(new Pagination<>(
                result.getContent(),
                new PaginationMeta(page, size, result.getTotalPages(), result.getTotalElements())
        ));
    }

    @PostMapping("/movimientos")
    public ResponseEntity<?> createMovimiento(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody MovimientoCreateDto dto) {
        return ResponseEntity.ok(finanzasService.createMovimiento(user, dto));
    }
}