package com.server.app.services;

import com.server.app.dto.finanzas.CuentaCreateDto;
import com.server.app.dto.finanzas.MovimientoCreateDto;
import com.server.app.dto.finanzas.TransferenciaDto;
import com.server.app.entities.Categoria;
import com.server.app.entities.Cuenta;
import com.server.app.entities.Movimiento;
import com.server.app.entities.User;
import com.server.app.exceptions.ConfictException;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.CategoriaRepository;
import com.server.app.repositories.CuentaRepository;
import com.server.app.repositories.MovimientoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class FinanzasService {

    private final CuentaRepository cuentaRepository;
    private final CategoriaRepository categoriaRepository;
    private final MovimientoRepository movimientoRepository;

    public Page<Cuenta> getCuentas(User usuario, int page, int size) {
        return cuentaRepository.findByUsuarioId(usuario.getId(), PageRequest.of(page, size));
    }

    @Transactional
    public Cuenta createCuenta(User usuario, CuentaCreateDto dto) {
        if (!dto.getTipo().equals("Ahorro") && !dto.getTipo().equals("Corriente")) {
            throw new ConfictException("El tipo de cuenta debe ser Ahorro o Corriente");
        }
        Cuenta cuenta = new Cuenta();
        cuenta.setAlias(dto.getAlias());
        cuenta.setMoneda(dto.getMoneda());
        cuenta.setSaldoBase(dto.getSaldoBase());
        cuenta.setTipo(dto.getTipo());
        cuenta.setUsuario(usuario);
        return cuentaRepository.save(cuenta);
    }

    public Page<Movimiento> getMovimientos(User usuario, int page, int size,
                                           LocalDateTime fechaInicio,
                                           LocalDateTime fechaFin) {
        return movimientoRepository.findByUsuarioIdAndFecha(
                usuario.getId(), fechaInicio, fechaFin, PageRequest.of(page, size));
    }

    @Transactional
    public Movimiento createMovimiento(User usuario, MovimientoCreateDto dto) {
        Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        if (cuenta.getUsuario().getId() != usuario.getId()) {
            throw new ConfictException("La cuenta no pertenece al usuario");
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada"));

        Movimiento movimiento = new Movimiento();
        movimiento.setMonto(dto.getMonto());
        movimiento.setMonedaOriginal(dto.getMonedaOriginal());
        movimiento.setTasaCambio(dto.getTasaCambio());
        movimiento.setDescripcion(dto.getDescripcion());
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setCuenta(cuenta);
        movimiento.setCategoria(categoria);

        if (categoria.getTipo().equals("Ingreso")) {
            cuenta.setSaldoBase(cuenta.getSaldoBase() + dto.getMonto());
        } else {
            if (cuenta.getSaldoBase() < dto.getMonto()) {
                throw new ConfictException("Saldo insuficiente");
            }
            cuenta.setSaldoBase(cuenta.getSaldoBase() - dto.getMonto());
        }

        cuentaRepository.save(cuenta);
        return movimientoRepository.save(movimiento);
    }

    @Transactional
    public Movimiento transferir(User usuario, TransferenciaDto dto) {
        Cuenta origen = cuentaRepository.findById(dto.getCuentaOrigenId())
                .orElseThrow(() -> new NotFoundException("Cuenta origen no encontrada"));

        Cuenta destino = cuentaRepository.findById(dto.getCuentaDestinoId())
                .orElseThrow(() -> new NotFoundException("Cuenta destino no encontrada"));

        if (origen.getUsuario().getId() != usuario.getId()) {
            throw new ConfictException("La cuenta origen no pertenece al usuario");
        }

        if (origen.getSaldoBase() < dto.getMonto()) {
            throw new ConfictException("Saldo insuficiente para realizar la transferencia");
        }

        origen.setSaldoBase(origen.getSaldoBase() - dto.getMonto());
        destino.setSaldoBase(destino.getSaldoBase() + dto.getMonto());

        cuentaRepository.save(origen);
        cuentaRepository.save(destino);

        Movimiento movimiento = new Movimiento();
        movimiento.setMonto(dto.getMonto());
        movimiento.setMonedaOriginal(origen.getMoneda());
        movimiento.setTasaCambio(1.0);
        movimiento.setDescripcion(dto.getDescripcion() != null
                ? dto.getDescripcion()
                : "Transferencia a " + destino.getAlias());
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setCuenta(origen);

        Categoria categoria = categoriaRepository.findById(1)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada, crea una primero"));
        movimiento.setCategoria(categoria);

        return movimientoRepository.save(movimiento);
    }

    public Page<Categoria> getCategorias(int page, int size) {
        return categoriaRepository.findAll(PageRequest.of(page, size));
    }
}