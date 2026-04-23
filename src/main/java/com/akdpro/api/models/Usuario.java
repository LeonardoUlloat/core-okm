package com.akdpro.api.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 12)
    private String rut; // Formato: 12.345.678-9

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    private String telefono;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol; // "ADMIN", "ALUMNO", "PROFE"

    private Boolean activo = true;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }

    // RELACIONES:

    // Muchos usuarios pertenecen a una Sede
    @ManyToOne
    @JoinColumn(name = "sede_id")
    private Sede sede;

    // Un usuario puede tener muchos pagos
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnore // Evita bucles infinitos al convertir a JSON
    private List<Pago> pagos;
}
