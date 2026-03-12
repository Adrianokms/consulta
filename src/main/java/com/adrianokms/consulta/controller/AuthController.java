package com.adrianokms.consulta.controller;

import com.adrianokms.consulta.domain.Usuario;
import com.adrianokms.consulta.dto.LoginRequestDTO;
import com.adrianokms.consulta.dto.LoginResponseDTO;
import com.adrianokms.consulta.dto.MensagemDTO;
import com.adrianokms.consulta.dto.UsuarioRequestDTO;
import com.adrianokms.consulta.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDto) {
        LoginResponseDTO response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registrar")
    public ResponseEntity<MensagemDTO> registrar(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = authService.registrar(dto);
        return ResponseEntity.ok(new MensagemDTO("Usuário registrado com sucesso: " + usuario.getNome()));
    }
}