package com.akdpro.api.dto;

import lombok.Data;

@Data
public class AsistenciaDTO {
    private Long usuarioId;
    private Long sedeId;
    private String fecha; // Viene como String desde el Front "YYYY-MM-DD"
    private String estado;
}