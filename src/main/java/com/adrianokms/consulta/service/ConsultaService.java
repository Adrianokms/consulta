package com.adrianokms.consulta.service;

import com.adrianokms.consulta.domain.Consulta;
import com.adrianokms.consulta.domain.Medico;
import com.adrianokms.consulta.domain.Paciente;
import com.adrianokms.consulta.dto.ConsultaRequestDTO;
import com.adrianokms.consulta.dto.ConsultaResponseDTO;
import com.adrianokms.consulta.exception.BusinessException;
import com.adrianokms.consulta.exception.ResourceNotFoundException;
import com.adrianokms.consulta.repository.ConsultaRepository;
import com.adrianokms.consulta.repository.MedicoRepository;
import com.adrianokms.consulta.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public ConsultaResponseDTO agendar(Long pacienteId, ConsultaRequestDTO dto) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));

        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));

        LocalDateTime data = dto.getDataHora();
        if (data.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Data da consulta deve ser futura");
        }

        boolean conflito = consultaRepository.existsByMedicoAndDataHoraAndStatus(medico, data, Consulta.StatusConsulta.AGENDADA);
        if (conflito) {
            throw new BusinessException("Médico já possui consulta agendada nesse horário");
        }

        if (data.getHour() < 8 || data.getHour() >= 18 || data.getDayOfWeek().getValue() >= 6) {
            throw new BusinessException("Horário fora do expediente (seg-sex, 8h-18h)");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(data);
        consulta.setObservacoes(dto.getObservacoes());
        consulta.setStatus(Consulta.StatusConsulta.AGENDADA);

        consulta = consultaRepository.save(consulta);

        emailService.enviarNotificacaoAgendamento(consulta);

        return ConsultaResponseDTO.fromEntity(consulta);
    }

    @Transactional
    public void cancelar(Long consultaId, Long pacienteId) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));

        if (!consulta.getPaciente().getId().equals(pacienteId)) {
            throw new BusinessException("Você não tem permissão para cancelar esta consulta");
        }

        if (consulta.getStatus() != Consulta.StatusConsulta.AGENDADA) {
            throw new BusinessException("Apenas consultas agendadas podem ser canceladas");
        }

        consulta.setStatus(Consulta.StatusConsulta.CANCELADA);
        consultaRepository.save(consulta);

        emailService.enviarNotificacaoCancelamento(consulta);
    }

    public List<ConsultaResponseDTO> listarConsultasDoPaciente(Long pacienteId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado"));
        return consultaRepository.findByPaciente(paciente)
                .stream()
                .map(ConsultaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ConsultaResponseDTO> listarConsultasDoMedico(Long medicoId) {
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado"));
        return consultaRepository.findByMedico(medico)
                .stream()
                .map(ConsultaResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<Medico> listarMedicosDisponiveis(LocalDateTime dataHora) {
        List<Medico> todosMedicos = medicoRepository.findAll();
        return todosMedicos.stream()
                .filter(medico -> !consultaRepository.existsByMedicoAndDataHoraAndStatus(medico, dataHora, Consulta.StatusConsulta.AGENDADA))
                .collect(Collectors.toList());
    }
}