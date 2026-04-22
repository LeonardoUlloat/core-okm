package com.akdpro.api.repositories;

import com.akdpro.api.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByRut(String rut);
    Optional<Usuario> findByEmail(String email);
}
