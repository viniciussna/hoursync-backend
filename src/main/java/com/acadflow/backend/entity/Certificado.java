package com.acadflow.backend.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "certificados")
public class Certificado {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private Integer horas;

    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    @Column(name = "arquivo_url")
    private String arquivoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCertificado status = StatusCertificado.PENDENTE;

    private String justificativa;

    private String turma;

    private String grupo;

    @Column(name = "codigo_atividade")
    private String codigoAtividade;

    @Column(name = "descricao_atividade", columnDefinition = "TEXT")
    private String descricaoAtividade;

    @Column(name = "horas_aprovadas")
    private Integer horasAprovadas;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "coordenador_id")
    private Usuario coordenador;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
}