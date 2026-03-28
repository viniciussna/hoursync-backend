package com.acadflow.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressoAlunoDTO {
    private String alunoId;
    private String nomeAluno;
    private Integer horasAcumuladas;
    private Integer horasExigidas;
    private Double percentual;
    private String status;
}