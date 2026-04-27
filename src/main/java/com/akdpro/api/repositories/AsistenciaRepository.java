package com.akdpro.api.repositories;

import com.akdpro.api.models.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    // Aquí podrías agregar métodos para buscar por fecha o por usuario más adelante
    List<Asistencia> findBySedeIdAndFecha(Long sedeId, LocalDate fecha);

    Optional<Asistencia> findByUsuarioIdAndSedeIdAndFecha(Long usuarioId, Long sedeId, LocalDate fecha);
}