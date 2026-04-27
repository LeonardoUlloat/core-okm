package com.akdpro.api.services;

import com.akdpro.api.dto.AsistenciaDTO;
import com.akdpro.api.models.Asistencia;
import com.akdpro.api.repositories.AsistenciaRepository;
import com.akdpro.api.repositories.SedeRepository;
import com.akdpro.api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private SedeRepository sedeRepository;

    // En AsistenciaService.java
    // AsistenciaService.java

    public void guardarMasiva(List<AsistenciaDTO> dtos) {
        for (AsistenciaDTO dto : dtos) {
            LocalDate fecha = LocalDate.parse(dto.getFecha());

            // 1. Intentamos buscar un registro existente
            Asistencia asistencia = asistenciaRepository
                    .findByUsuarioIdAndSedeIdAndFecha(dto.getUsuarioId(), dto.getSedeId(), fecha)
                    .orElse(new Asistencia()); // Si no existe, creamos una instancia vacía

            // 2. Seteamos los valores (si la asistencia existía, JPA mantendrá el ID original y hará un UPDATE)
            asistencia.setFecha(fecha);
            asistencia.setEstado(dto.getEstado());

            // Buscamos las entidades relacionadas
            asistencia.setUsuario(usuarioRepository.findById(dto.getUsuarioId()).orElse(null));
            asistencia.setSede(sedeRepository.findById(dto.getSedeId()).orElse(null));

            // 3. Guardamos (Spring hará INSERT si es nuevo o UPDATE si ya tenía ID)
            asistenciaRepository.save(asistencia);
        }
    }

    public List<Asistencia> buscarPorSedeYFecha(Long sedeId, LocalDate fecha) {
        return asistenciaRepository.findBySedeIdAndFecha(sedeId, fecha);
    }
}