package com.akdpro.api.repositories;

import com.akdpro.api.models.Invitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InvitacionRepository extends JpaRepository<Invitacion, Long> {
    Optional<Invitacion> findByToken(String token);
}
