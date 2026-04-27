package com.akdpro.api.controllers;

import com.akdpro.api.dto.AsistenciaDTO;
import com.akdpro.api.models.Asistencia;
import com.akdpro.api.services.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/asistencias")
@CrossOrigin(origins = "http://localhost:4200")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @PostMapping("/masiva")
    public ResponseEntity<?> registrarAsistencia(@RequestBody List<AsistenciaDTO> asistenciaLista) {
        try {
            asistenciaService.guardarMasiva(asistenciaLista);
            return ResponseEntity.ok("{\"message\": \"Asistencia registrada correctamente\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // AsistenciaController.java
    @GetMapping("/historial/{sedeId}/{fecha}")
    public ResponseEntity<List<Asistencia>> obtenerAsistencia(
            @PathVariable Long sedeId,
            @PathVariable String fecha) {
        try {
            LocalDate date = LocalDate.parse(fecha);
            List<Asistencia> lista = asistenciaService.buscarPorSedeYFecha(sedeId, date);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}