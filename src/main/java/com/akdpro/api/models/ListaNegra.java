package com.akdpro.api.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "lista_negra")
@Data
public class ListaNegra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 12)
    private String rut;

    // Guardamos el nombre por si el usuario ya no existe en la tabla 'usuarios'
    private String nombreCompleto;

    @Column(columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "fecha_bloqueo")
    private LocalDateTime fechaBloqueo = LocalDateTime.now();

    private String adminResponsable; // Nombre o email del admin que lo bloqueó
}
