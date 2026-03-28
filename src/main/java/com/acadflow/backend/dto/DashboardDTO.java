package com.acadflow.backend.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private long totalCursos;
    private long totalCoordenadores;
    private long totalAlunos;
    private long totalCertificados;
    private long certificadosPendentes;
    private long certificadosAprovados;
    private long certificadosRejeitados;
    private long horasValidadas;
    private double taxaAprovacao;
}
