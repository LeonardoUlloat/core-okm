package com.akdpro.api.repositories;

import com.akdpro.api.models.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    // Para buscar todos los pagos de un alumno específico
    List<Pago> findByUsuarioId(Long usuarioId);
}
