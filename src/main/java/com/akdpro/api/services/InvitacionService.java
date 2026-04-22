package com.akdpro.api.services;

import com.akdpro.api.models.Invitacion;
import com.akdpro.api.repositories.InvitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class InvitacionService {

    @Autowired
    private InvitacionRepository invitacionRepository;

    public String generarLink(String rol, Integer horas, Integer maxUsos) {
        // Si el admin no manda horas, le damos 24 por defecto
        int horasValidez = (horas != null) ? horas : 24;
        int max = (maxUsos != null && maxUsos > 0) ? maxUsos : 1;

        Invitacion inv = new Invitacion(rol, horasValidez, max);
        invitacionRepository.save(inv);

        return "http://localhost:4200/registro?token=" + inv.getToken();
    }


    public Invitacion validarToken(String token) throws Exception {
        // Limpieza de URL por si acaso
        if (token.contains("token=")) {
            token = token.substring(token.indexOf("token=") + 6);
        }

        Invitacion inv = invitacionRepository.findByToken(token)
                .orElseThrow(() -> new Exception("Enlace no válido."));

        // REGLA: ¿Quedan cupos?
        if (inv.getUsosActuales() >= inv.getUsosMaximos()) {
            throw new Exception("Se ha alcanzado el límite de inscripciones para este enlace.");
        }

        if (inv.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new Exception("El enlace ha expirado.");
        }

        return inv;
    }

    public void marcarComoUsada(Invitacion inv) {
        inv.setUsosActuales(inv.getUsosActuales() + 1);
        invitacionRepository.save(inv);
    }

}