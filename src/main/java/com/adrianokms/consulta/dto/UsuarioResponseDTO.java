package com.adrianokms.consulta.dto;

import com.adrianokms.consulta.domain.Role;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private Role role;
    private String telefone;
    private String dataNascimento;
    private String crm;
    private String especialidade;
}