package com.akdpro.api.services;

import com.akdpro.api.dto.LoginDTO;
import com.akdpro.api.models.Usuario;
import com.akdpro.api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder; // Agrega esta inyección arriba

    @Autowired // <--- ESTO ES LO QUE FALTA
    private UsuarioRepository usuarioRepository;

    public Usuario login(LoginDTO loginDto) throws Exception {
        // 1. Buscamos al usuario por email
        Usuario usuario = usuarioRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // 2. COMPARACIÓN SEGURA (Ya no es texto plano)
        // El orden importa: (Contraseña_Limpia, Contraseña_Encriptada_DB)
        if (!passwordEncoder.matches(loginDto.getPassword(), usuario.getPassword())) {
            throw new Exception("Contraseña incorrecta");
        }

        // 3. Si todo está ok, devolvemos el usuario
        return usuario;
    }
}
