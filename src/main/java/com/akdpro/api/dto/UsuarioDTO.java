package com.akdpro.api.dto;

import java.time.LocalDateTime;

public class UsuarioDTO {
    private String nombreCompleto;
    private String sedeNombre;
    private LocalDateTime fechaRegistro;

    // Constructor vacío
    public UsuarioDTO() {}

    // Constructor con parámetros (ESTO ES LO QUE USA EL MAP EN EL SERVICE)
    public UsuarioDTO(String nombreCompleto, String sedeNombre, LocalDateTime fechaRegistro) {
        this.nombreCompleto = nombreCompleto;
        this.sedeNombre = sedeNombre;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getSedeNombre() { return sedeNombre; }
    public void setSedeNombre(String sedeNombre) { this.sedeNombre = sedeNombre; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}