package com.akdpro.api.controllers;

import com.akdpro.api.models.Usuario;
import com.akdpro.api.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200") // Permite conexión con Angular
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Listar todos (Útil para el Admin)
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    // Buscar por RUT específico
    @GetMapping("/rut/{rut}")
    public ResponseEntity<?> obtenerPorRut(@PathVariable String rut) {
        return usuarioService.buscarPorRut(rut)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Registro de nuevo usuario (Aplica todas las reglas de negocio)
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.registrar(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            // Enviamos el mensaje de error (ej: "Está en lista negra") al frontend
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Resetear contraseña (Solo debería ser accesible por Admin)
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestBody String nuevaPass) {
        try {
            usuarioService.cambiarPassword(id, nuevaPass);
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}