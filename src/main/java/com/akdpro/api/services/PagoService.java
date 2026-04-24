package com.akdpro.api.services; // Asegúrate de que el package sea correcto

import com.akdpro.api.models.Pago;
import com.akdpro.api.models.Usuario;
import com.akdpro.api.repositories.PagoRepository;
import com.akdpro.api.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service // Indispensable para que Spring lo inyecte
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Genera deudas para todos los alumnos.
     * Se ejecuta automáticamente el día 1 de cada mes a las 00:00
     */
    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional // Asegura que si algo falla, no se guarden datos a medias
    public void generarMensualidades() {
        // Buscamos solo alumnos activos para no cobrar a gente retirada
        List<Usuario> alumnos = usuarioRepository.findByRolAndActivoTrue("ALUMNO");

        String mesActual = LocalDate.now().getMonth().toString();
        int anioActual = LocalDate.now().getYear();

        for (Usuario alumno : alumnos) {
            // Verificar duplicados para evitar cobrar doble si se reinicia el servidor
            if (pagoRepository.findByUsuarioIdAndMesAndAnio(alumno.getId(), mesActual, anioActual).isEmpty()) {
                Pago deuda = new Pago();
                deuda.setUsuario(alumno);
                deuda.setMes(mesActual);
                deuda.setAnio(anioActual);
                deuda.setMonto(50000.0); // Aquí podrías usar una variable configurable
                deuda.setEstado("PENDIENTE");
                pagoRepository.save(deuda);
            }
        }
    }

    /**
     * Registra el pago realizado por un alumno
     */
    @Transactional
    public Pago procesarPago(Long pagoId, String metodo) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Error: Registro de pago no encontrado con ID: " + pagoId));

        pago.setEstado("PAGADO");
        pago.setMetodoPago(metodo);
        pago.setFechaPago(LocalDateTime.now());

        return pagoRepository.save(pago);
    }
}