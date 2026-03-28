package com.acadflow.backend.repository;
import com.acadflow.backend.entity.Certificado;
import com.acadflow.backend.entity.StatusCertificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificadoRepository extends JpaRepository<Certificado, String> {
    List<Certificado> findByAlunoId(String alunoId);
    List<Certificado> findByCursoId(String cursoId);
    List<Certificado> findByStatus(StatusCertificado status);
    List<Certificado> findByCursoIdAndStatus(String cursoId, StatusCertificado status);
    long countByStatus(StatusCertificado status);
    long countByCursoId(String cursoId);

    @Query("SELECT SUM(c.horas) FROM Certificado c WHERE c.aluno.id = :alunoId AND c.status = 'APROVADO'")
    Integer totalHorasAprovadasPorAluno(String alunoId);

    @Query("SELECT SUM(c.horas) FROM Certificado c WHERE c.curso.id = :cursoId AND c.status = 'APROVADO'")
    Integer totalHorasAprovadasPorCurso(String cursoId);
}
