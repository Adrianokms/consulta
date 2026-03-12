package com.adrianokms.consulta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tipo; // "Bearer"
    private Long id;
    private String nome;
    private String role;
}