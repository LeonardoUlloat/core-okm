package com.akdpro.api.dto;

public class AlumnoGestionDTO {
    private Long id;
    private String rut;
    private String nombreCompleto;
    private String email;
    private String sedeNombre;

    public AlumnoGestionDTO() {}

    public AlumnoGestionDTO(Long id, String rut, String nombreCompleto, String email, String sedeNombre) {
        this.id = id;
        this.rut = rut;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.sedeNombre = sedeNombre;
    }

    public Long getId() { return id; }
    public String getRut() { return rut; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getEmail() { return email; }
    public String getSedeNombre() { return sedeNombre; }

    public void setId(Long id) { this.id = id; }
    public void setRut(String rut) { this.rut = rut; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setEmail(String email) { this.email = email; }
    public void setSedeNombre(String sedeNombre) { this.sedeNombre = sedeNombre; }
}
