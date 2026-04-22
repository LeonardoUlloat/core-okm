package com.akdpro.api.repositories;

import com.akdpro.api.models.ListaNegra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ListaNegraRepository extends JpaRepository<ListaNegra, Long> {

    // Comprueba si existe el RUT para bloquear el registro
    boolean existsByRut(String rut);

    // Por si el Admin quiere buscar la ficha de bloqueo por RUT
    Optional<ListaNegra> findByRut(String rut);
}
