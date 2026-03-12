package com.adrianokms.consulta.repository;

import com.adrianokms.consulta.domain.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    List<Medico> findByEspecialidade(String especialidade);
}