package com.akdpro.api.controllers;

import com.akdpro.api.models.*;
import com.akdpro.api.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private SedeService sedeService;
    @Autowired private TiendaService tiendaService;
    @Autowired private ListaNegraService listaNegraService;
    @Autowired private InvitacionService invitacionService;

    // ==========================================
    // 1. GESTIÓN DE USUARIOS (ALUMNOS/PROFES)
    // ==========================================

    @GetMapping("/usuarios")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarTodos();
    }

    @PutMapping("/usuarios/reset-password/{id}")
    public ResponseEntity<?> resetearClave(@PathVariable Long id, @RequestBody String nuevaPass) {
        try {
            usuarioService.cambiarPassword(id, nuevaPass);
            return ResponseEntity.ok("Contraseña reseteada por el Administrador.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==========================================
    // 2. GESTIÓN DE LISTA NEGRA (EL BANEO)
    // ==========================================

    @PostMapping("/lista-negra/banear")
    // Cambiamos a RequestBody para recibir un JSON desde el front
    public ResponseEntity<?> banearAlumno(@RequestBody RequestBaneo request) {
        try {
            listaNegraService.agregarAListaNegra(request.getRut(), request.getMotivo(), "ADMIN_GENERAL");
            return ResponseEntity.ok("El alumno ha sido expulsado y movido a la Lista Negra.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Clase auxiliar (DTO) para recibir los datos del baneo
    public static class RequestBaneo {
        private String rut;
        private String motivo;
        // getters y setters
        public String getRut() { return rut; }
        public String getMotivo() { return motivo; }
    }

    @GetMapping("/lista-negra")
    public List<ListaNegra> verListaNegra() {
        return listaNegraService.listarBloqueados(); // Asegúrate de tener este método en el service
    }

    // ==========================================
    // 3. GESTIÓN DE SEDES
    // ==========================================

    @PostMapping("/sedes")
    public Sede crearSede(@RequestBody Sede sede) {
        return sedeService.crearSede(sede);
    }

    @PutMapping("/sedes/{id}")
    public ResponseEntity<?> editarSede(@PathVariable Long id, @RequestBody Sede sede) {
        try {
            return ResponseEntity.ok(sedeService.actualizarSede(id, sede));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/sedes") // <--- ESTE ES EL QUE FALTA
    public List<Sede> listarSedes() {
        return sedeService.listarSedes();
    }

    @DeleteMapping("/sedes/{id}")
    public ResponseEntity<?> borrarSede(@PathVariable Long id) {
        sedeService.eliminarSede(id);
        return ResponseEntity.ok("Sede eliminada.");
    }

    // ==========================================
    // 4. GESTIÓN DE TIENDA (ACCESORIOS)
    // ==========================================

    @PostMapping("/productos")
    public Producto agregarProducto(@RequestBody Producto producto) {
        return tiendaService.agregarAccesorio(producto);
    }

    @PatchMapping("/productos/{id}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable Long id, @RequestBody Integer nuevoStock) {
        try {
            tiendaService.actualizarStock(id, nuevoStock);
            return ResponseEntity.ok("Stock actualizado.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generar")
    public ResponseEntity<String> generarLink(
            @RequestParam String rol,
            @RequestParam(required = false) Integer horas,
            @RequestParam(required = false) Integer maximos) { // Nuevo parámetro

        String link = invitacionService.generarLink(rol, horas, maximos);
        return ResponseEntity.ok(link);
    }

    @GetMapping("/dashboard/resumen")
    public ResponseEntity<Map<String, Object>> getDashboardResumen() {
        // El controlador solo pregunta y responde
        return ResponseEntity.ok(usuarioService.obtenerEstadisticasDashboard());
    }

    // ==========================================
    // 5. ESTADÍSTICAS DASHBOARD
    // ==========================================

    @GetMapping("/alumnos/count")
    public ResponseEntity<Long> countAlumnos() {
        // Asumiendo que tus alumnos están en la tabla Usuario con un rol específico
        // Si no tienes el método en el service, puedes usar listarUsuarios().size() temporalmente
        long total = usuarioService.listarTodos().stream()
                .filter(u -> "ALUMNO".equals(u.getRol())) // Ajusta según tus roles
                .count();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/mensualidades/total-mes")
    public ResponseEntity<Double> getTotalMes() {
        // Por ahora devolvemos 0 para que Angular no de error 404
        // Más adelante inyectarás el servicio de pagos aquí
        return ResponseEntity.ok(0.0);
    }

}
