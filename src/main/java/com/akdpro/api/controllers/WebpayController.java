package com.akdpro.api.controllers;

import com.akdpro.api.models.Pago;
import com.akdpro.api.repositories.PagoRepository;
import com.akdpro.api.services.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/webpay") // Ruta pública
public class WebpayController {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PagoService pagoService;

    @PostMapping("/confirmar")
    @Transactional
    public ResponseEntity<String> confirmarPago(@RequestParam("token_ws") String tokenWs) {
        // Buscamos el pago por el token que guardamos al iniciar la compra
        Pago pago = pagoRepository.findByTokenWebpay(tokenWs)
                .orElseThrow(() -> new RuntimeException("Token no encontrado"));

        // Aquí procesamos el pago como 'WEBPAY'
        pagoService.procesarPago(pago.getId(), "WEBPAY");

        // Webpay requiere que le devuelvas una respuesta exitosa
        return ResponseEntity.ok("OK");
    }
}