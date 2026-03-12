package com.adrianokms.consulta.controller;

import com.adrianokms.consulta.domain.Medico;
import com.adrianokms.consulta.domain.Usuario;
import com.adrianokms.consulta.dto.ConsultaRequestDTO;
import com.adrianokms.consulta.dto.ConsultaResponseDTO;
import com.adrianokms.consulta.dto.MensagemDTO;
import com.adrianokms.consulta.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    private Long extrairId(UserDetails userDetails) {
        if (userDetails instanceof Usuario usuario) {
            return usuario.getId();
        }
        throw new IllegalArgumentException("UserDetails não contém ID");
    }

    @PostMapping("/agendar")
    public ResponseEntity<ConsultaResponseDTO> agendar(
            @AuthenticationPrincipal UserDetails currentUser,
            @Valid @RequestBody ConsultaRequestDTO dto) {
        Long pacienteId = extrairId(currentUser);
        ConsultaResponseDTO consulta = consultaService.agendar(pacienteId, dto);
        return ResponseEntity.ok(consulta);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<MensagemDTO> cancelar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails currentUser) {
        Long pacienteId = extrairId(currentUser);
        consultaService.cancelar(id, pacienteId);
        return ResponseEntity.ok(new MensagemDTO("Consulta cancelada com sucesso"));
    }

    @GetMapping("/medico")
    public ResponseEntity<List<ConsultaResponseDTO>> listarConsultasDoMedico(
            @AuthenticationPrincipal UserDetails currentUser) {
        Long medicoId = extrairId(currentUser);
        List<ConsultaResponseDTO> consultas = consultaService.listarConsultasDoMedico(medicoId);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/paciente")
    public ResponseEntity<List<ConsultaResponseDTO>> listarMinhasConsultas(
            @AuthenticationPrincipal UserDetails currentUser) {
        Long pacienteId = extrairId(currentUser);
        List<ConsultaResponseDTO> consultas = consultaService.listarConsultasDoPaciente(pacienteId);
        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/medico/disponiveis")
    public ResponseEntity<List<Medico>> medicosDisponiveis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora) {
        List<Medico> medicos = consultaService.listarMedicosDisponiveis(dataHora);
        return ResponseEntity.ok(medicos);
    }
}