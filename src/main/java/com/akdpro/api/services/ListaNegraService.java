package com.akdpro.api.services;

import com.akdpro.api.models.ListaNegra;
import com.akdpro.api.models.Usuario;
import com.akdpro.api.repositories.ListaNegraRepository;
import com.akdpro.api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ListaNegraService {

    @Autowired
    private ListaNegraRepository listaNegraRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void agregarAListaNegra(String rut, String motivo, String adminNombre) throws Exception {
        // 1. Limpiar RUT para buscarlo
        String rutLimpio = rut.replace(".", "").replace("-", "").toUpperCase();

        // 2. Buscar si el alumno existe actualmente
        Usuario alumno = usuarioRepository.findByRut(rutLimpio)
                .orElseThrow(() -> new Exception("No se puede banear: El RUT no existe en la base de datos de alumnos."));

        // 3. Crear el registro de baneo con el historial
        ListaNegra baneo = new ListaNegra();
        baneo.setRut(alumno.getRut());
        baneo.setNombreCompleto(alumno.getNombreCompleto()); // Guardamos el nombre para el registro histórico
        baneo.setMotivo(motivo);
        baneo.setAdminResponsable(adminNombre);

        listaNegraRepository.save(baneo);

        // 4. ELIMINAR al usuario de la tabla activa para que no pueda entrar más
        usuarioRepository.delete(alumno);
    }

    public List<ListaNegra> listarBloqueados() {
        return listaNegraRepository.findAll();
    }

    public void quitarDeListaNegra(Long id) {
        listaNegraRepository.deleteById(id);
    }
}
