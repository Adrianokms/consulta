package com.adrianokms.consulta.service;

import com.adrianokms.consulta.domain.Medico;
import com.adrianokms.consulta.domain.Paciente;
import com.adrianokms.consulta.domain.Role;
import com.adrianokms.consulta.domain.Usuario;
import com.adrianokms.consulta.dto.LoginRequestDTO;
import com.adrianokms.consulta.dto.LoginResponseDTO;
import com.adrianokms.consulta.dto.UsuarioRequestDTO;
import com.adrianokms.consulta.repository.MedicoRepository;
import com.adrianokms.consulta.repository.PacienteRepository;
import com.adrianokms.consulta.repository.UsuarioRepository;
import com.adrianokms.consulta.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        Usuario usuario = usuarioRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new LoginResponseDTO(token, "Bearer", usuario.getId(), usuario.getNome(), usuario.getRole().name());
    }

    public Usuario registrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario;
        if (dto.getRole() == Role.PACIENTE) {
            Paciente paciente = new Paciente();
            paciente.setTelefone(dto.getTelefone());
            paciente.setDataNascimento(dto.getDataNascimento());
            usuario = paciente;
        } else if (dto.getRole() == Role.MEDICO) {
            Medico medico = new Medico();
            medico.setCrm(dto.getCrm());
            medico.setEspecialidade(dto.getEspecialidade());
            usuario = medico;
        } else {
            throw new RuntimeException("Role inválida");
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setRole(dto.getRole());

        return usuarioRepository.save(usuario);
    }
}