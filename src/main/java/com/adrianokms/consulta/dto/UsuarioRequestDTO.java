package com.adrianokms.consulta.dto;

import com.adrianokms.consulta.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioRequestDTO {
    @NotBlank
    private String nome;
    @NotBlank @Email
    private String email;
    @NotBlank
    private String senha;
    @NotNull
    private Role role; // PACIENTE ou MEDICO

    // Campos específicos
    private String telefone;
    private String dataNascimento;
    private String crm;
    private String especialidade;
}