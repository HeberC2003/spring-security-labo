package com.server.app.repositories;

import com.server.app.entities.Movimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.usuario.id = :usuarioId " +
            "AND (:fechaInicio IS NULL OR m.fecha >= :fechaInicio) " +
            "AND (:fechaFin IS NULL OR m.fecha <= :fechaFin)")
    Page<Movimiento> findByUsuarioIdAndFecha(
            @Param("usuarioId") int usuarioId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            Pageable pageable);

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.usuario.id = :usuarioId")
    Page<Movimiento> findByUsuarioId(
            @Param("usuarioId") int usuarioId,
            Pageable pageable);
}