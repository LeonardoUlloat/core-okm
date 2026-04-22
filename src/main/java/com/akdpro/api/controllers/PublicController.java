package com.akdpro.api.controllers;

import com.akdpro.api.models.Usuario;
import com.akdpro.api.services.InvitacionService;
import com.akdpro.api.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "http://localhost:4200")
public class PublicController {

    @Autowired private UsuarioService usuarioService;
    @Autowired private InvitacionService invitacionService;

    // 1. Para que Angular verifique si el link es válido apenas el alumno abre el enlace
    @GetMapping("/validar-token/{token}")
    public ResponseEntity<?> validarToken(@PathVariable String token) {
        try {
            return ResponseEntity.ok(invitacionService.validarToken(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Para procesar el formulario de registro final
    @PostMapping("/registro-alumno")
    public ResponseEntity<?> registrarConToken(@RequestBody Usuario usuario, @RequestParam String token) {
        try {
            Usuario nuevo = usuarioService.registrarConInvitacion(usuario, token);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
