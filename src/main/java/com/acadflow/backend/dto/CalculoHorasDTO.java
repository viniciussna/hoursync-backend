package com.acadflow.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculoHorasDTO {
    private String alunoId;
    private String nomeAluno;
    private Integer horasSolicitadas;
    private Integer horasUtilizadasSemestre;
    private Integer limiteSemestral;
    private Integer horasDisponiveis;
    private Integer horasQueSeraoAprovadas;
    private String mensagem;
}