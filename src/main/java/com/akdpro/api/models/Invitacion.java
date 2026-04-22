package com.akdpro.api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invitaciones")
@Data
@NoArgsConstructor // Constructor vacío para Hibernate
@AllArgsConstructor // Constructor con todos los campos
public class Invitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private String rol;

    private boolean usado = false;

    @Column(nullable = false)
    private int usosMaximos;

    @Column(nullable = false)
    private int usosActuales;

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    // Tu constructor manual que usa el Service
    public Invitacion(String rol, int horasValidez, int maximos) {
        this.token = UUID.randomUUID().toString();
        this.rol = rol;
        this.usosMaximos = maximos;
        this.usosActuales = 0;
        this.usado = false;
        this.fechaExpiracion = LocalDateTime.now().plusHours(horasValidez);
    }
}