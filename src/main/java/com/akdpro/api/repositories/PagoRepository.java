package com.akdpro.api.repositories;

import com.akdpro.api.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface
PagoRepository extends JpaRepository<Pago, Long> {
    // Para buscar todos los pagos de un alumno específico
    List<Pago> findByUsuarioId(Long usuarioId);

    // Para las estadísticas y generación de deudas
    List<Pago> findByMesAndAnio(String mes, Integer anio);

    // Para evitar duplicar deudas (verificar si ya se le cargó el mes al alumno)
    Optional<Pago> findByUsuarioIdAndMesAndAnio(Long usuarioId, String mes, Integer anio);
    Optional<Pago>findByTokenWebpay(String tokenWebpay);
}
