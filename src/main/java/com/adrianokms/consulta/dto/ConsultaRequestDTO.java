package com.adrianokms.consulta.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConsultaRequestDTO {
    @NotNull
    private Long medicoId;
    @NotNull
    @Future
    private LocalDateTime dataHora;
    private String observacoes;
}