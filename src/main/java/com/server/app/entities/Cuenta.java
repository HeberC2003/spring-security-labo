package com.server.app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cuentas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false)
    private String moneda;

    @Column(nullable = false)
    private double saldoBase;

    @Column(nullable = false)
    private String tipo; // Ahorro o Corriente

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;
}