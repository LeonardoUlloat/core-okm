package com.akdpro.api.dto;

import lombok.Data;

@Data // Lombok genera los getters y setters automáticamente
public class LoginDTO {
    private String email;
    private String password;
}
