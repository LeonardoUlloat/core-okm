package com.akdpro.api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sedes")
@Data
public class Sede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;
    private String ciudad;
}