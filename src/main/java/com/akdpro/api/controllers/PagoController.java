package com.akdpro.api.controllers;

import com.akdpro.api.models.Pago;
import com.akdpro.api.services.PagoService;
import com.akdpro.api.repositories.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/admin/pagos")
@CrossOrigin(origins = "http://localhost:4200")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PagoRepository pagoRepository;

    @GetMapping
    public List<Pago> listarTodos() {
        return pagoRepository.findAll();
    }

    // Nota: Aunque dijiste que es vía Webpay, dejamos esto por si acaso
    // el Admin recibe efectivo en la oficina física.
    @PutMapping("/{id}/registrar")
    public ResponseEntity<Pago> registrarPago(
            @PathVariable Long id,
            @RequestParam String metodo) {
        Pago pagoActualizado = pagoService.procesarPago(id, metodo);
        return ResponseEntity.ok(pagoActualizado);
    }

    @PostMapping("/generar-mes")
    public ResponseEntity<Map<String, String>> generarMensualidadesManual() {
        pagoService.generarMensualidades();
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Se han generado las deudas para los alumnos activos.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        List<Pago> pagos = pagoRepository.findAll();

        double recaudado = pagos.stream()
                .filter(p -> "PAGADO".equalsIgnoreCase(p.getEstado()))
                .mapToDouble(Pago::getMonto).sum();

        double pendiente = pagos.stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstado()))
                .mapToDouble(Pago::getMonto).sum();

        // Contamos cuántos alumnos únicos tienen deudas este mes
        long totalAlumnos = pagos.stream()
                .map(p -> p.getUsuario().getId())
                .distinct()
                .count();

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("recaudado", recaudado);
        resumen.put("pendiente", pendiente);
        resumen.put("totalAlumnos", totalAlumnos);

        return ResponseEntity.ok(resumen);
    }
}