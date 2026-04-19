package com.acadflow.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "horas_exigidas", nullable = false)
    private Integer horasExigidas;

    @Column(name = "horas_por_semestre")
    private Integer horasPorSemestre;
}