package com.adrianokms.consulta.repository;

import com.adrianokms.consulta.domain.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}