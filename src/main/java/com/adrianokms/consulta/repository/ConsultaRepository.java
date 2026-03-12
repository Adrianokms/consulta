package com.adrianokms.consulta.repository;

import com.adrianokms.consulta.domain.Consulta;
import com.adrianokms.consulta.domain.Medico;
import com.adrianokms.consulta.domain.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByPaciente(Paciente paciente);
    List<Consulta> findByMedico(Medico medico);

    @Query("SELECT c FROM Consulta c WHERE c.medico = :medico AND c.dataHora BETWEEN :inicio AND :fim AND c.status = 'AGENDADA'")
    List<Consulta> findAgendadasPorMedicoNoPeriodo(@Param("medico") Medico medico,
                                                   @Param("inicio") LocalDateTime inicio,
                                                   @Param("fim") LocalDateTime fim);

    boolean existsByMedicoAndDataHoraAndStatus(Medico medico, LocalDateTime dataHora, Consulta.StatusConsulta status);
}