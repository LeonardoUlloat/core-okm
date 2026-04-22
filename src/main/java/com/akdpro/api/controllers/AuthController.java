package com.akdpro.api.controllers;

import com.akdpro.api.dto.LoginDTO;
import com.akdpro.api.models.Invitacion;
import com.akdpro.api.models.Usuario;
import com.akdpro.api.services.AuthService;
import com.akdpro.api.services.InvitacionService;
import com.akdpro.api.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // Para conectar con tu Angular después
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @RequestParam String token,
            @RequestBody Usuario usuario) {
        try {
            // Llamamos al método que ya tiene toda la lógica (limpiar RUT, lista negra, validar token)
            Usuario usuarioCreado = usuarioService.registrarConInvitacion(usuario, token);

            return ResponseEntity.ok("¡Registro exitoso! Bienvenido como " + usuarioCreado.getRol());
        } catch (Exception e) {
            // Aquí capturamos los mensajes de "RUT en lista negra", "Token expirado", etc.
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        try {
            Usuario usuario = authService.login(loginDto);

            // Limpiamos la contraseña antes de responder para que no viaje por la red
            usuario.setPassword(null);

            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            // Si falla, mandamos un 401 (No autorizado)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
