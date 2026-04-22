package com.akdpro.api.services;

import com.akdpro.api.models.Invitacion;
import com.akdpro.api.models.Usuario;
import com.akdpro.api.repositories.UsuarioRepository;
import com.akdpro.api.repositories.ListaNegraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // IMPORTANTE
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ListaNegraRepository listaNegraRepository;

    @Autowired
    private InvitacionService invitacionService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyectamos la herramienta de BCrypt

    @Autowired
    private SedeService sedeService;

    @Transactional
    public Usuario registrar(Usuario usuario) throws Exception {
        String rutLimpio = usuario.getRut().replace(".", "").replace("-", "").toUpperCase();
        usuario.setRut(rutLimpio);

        if (listaNegraRepository.existsByRut(rutLimpio)) {
            throw new Exception("BLOQUEO: El RUT " + rutLimpio + " está en la lista negra.");
        }

        if (usuarioRepository.findByRut(rutLimpio).isPresent()) {
            throw new Exception("El RUT ya se encuentra registrado.");
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new Exception("El correo electrónico ya está en uso.");
        }

        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("ALUMNO");
        }

        // --- ENCRIPTACIÓN AQUÍ ---
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    public void cambiarPassword(Long id, String nuevaPassword) throws Exception {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // --- ENCRIPTACIÓN AQUÍ TAMBIÉN ---
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario registrarConInvitacion(Usuario usuario, String token) throws Exception {
        Invitacion inv = invitacionService.validarToken(token);

        String rutLimpio = usuario.getRut().replace(".", "").replace("-", "").toUpperCase();
        usuario.setRut(rutLimpio);

        if (listaNegraRepository.existsByRut(rutLimpio)) {
            throw new Exception("ACCESO DENEGADO: Este RUT está en lista negra.");
        }

        if (usuarioRepository.findByRut(rutLimpio).isPresent()) {
            throw new Exception("El RUT ya está registrado.");
        }

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new Exception("El Email ya está en uso.");
        }

        usuario.setRol(inv.getRol());

        // --- ENCRIPTACIÓN AQUÍ ---
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        invitacionService.marcarComoUsada(inv);

        return usuarioGuardado;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorRut(String rut) {
        return usuarioRepository.findByRut(rut.replace(".", "").replace("-", "").toUpperCase());
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Map<String, Object> obtenerEstadisticasDashboard() {
        Map<String, Object> stats = new HashMap<>();

        // 1. Conteo de Alumnos
        long totalAlumnos = usuarioRepository.findAll().stream()
                .filter(u -> "ALUMNO".equals(u.getRol()))
                .count();

        // 2. Conteo de Sedes
        long totalSedes = sedeService.listarSedes().size();

        // 3. Obtener los últimos 5 alumnos registrados para la lista
        // Filtramos por rol ALUMNO y tomamos los últimos de la lista
        List<Map<String, Object>> ultimasInscripciones = usuarioRepository.findAll().stream()
                .filter(u -> "ALUMNO".equals(u.getRol()))
                .sorted((u1, u2) -> u2.getId().compareTo(u1.getId())) // Ordenar por ID descendente (los más nuevos primero)
                .limit(5) // Solo los últimos 5
                .map(u -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombreAlumno", u.getNombreCompleto());
                    if (u.getSede() != null) {
                        map.put("sedeNombre", u.getSede().getNombre());
                    } else {
                        map.put("sedeNombre", "Sin Sede"); // O un valor por defecto
                    }
                    map.put("fecha", java.time.LocalDate.now().toString());

                    return map;
                })
                .toList();

        stats.put("totalAlumnos", totalAlumnos);
        stats.put("totalSedes", totalSedes);
        stats.put("porcentajePagos", 85);
        stats.put("ultimasInscripciones", ultimasInscripciones); // <--- AHORA VA CON DATOS

        return stats;
    }

}