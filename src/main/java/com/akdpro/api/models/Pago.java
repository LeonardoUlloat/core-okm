package com.akdpro.api.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Data
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mes;
    private Integer anio;
    private Double monto;
    private String estado; // "PAGADO" o "PENDIENTE"
    private String metodoPago;
    private LocalDateTime fechaPago;
    private String tokenWebpay;

    @ManyToOne
    @JoinColumn(name = "usuario_id") // Crea la llave foránea en la tabla pagos
    private Usuario usuario;
}
