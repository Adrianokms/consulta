package com.adrianokms.consulta.dto;

import com.adrianokms.consulta.domain.Consulta;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConsultaResponseDTO {
    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private Long medicoId;
    private String medicoNome;
    private String medicoEspecialidade;
    private LocalDateTime dataHora;
    private String status;
    private String observacoes;

    public static ConsultaResponseDTO fromEntity(Consulta consulta) {
        ConsultaResponseDTO dto = new ConsultaResponseDTO();
        dto.setId(consulta.getId());
        dto.setPacienteId(consulta.getPaciente().getId());
        dto.setPacienteNome(consulta.getPaciente().getNome());
        dto.setMedicoId(consulta.getMedico().getId());
        dto.setMedicoNome(consulta.getMedico().getNome());
        dto.setMedicoEspecialidade(consulta.getMedico().getEspecialidade());
        dto.setDataHora(consulta.getDataHora());
        dto.setStatus(consulta.getStatus().name());
        dto.setObservacoes(consulta.getObservacoes());
        return dto;
    }
}