package com.acadflow.backend.controller;

import com.acadflow.backend.dto.ProgressoAlunoDTO;
import com.acadflow.backend.entity.StatusCertificado;
import com.acadflow.backend.repository.CertificadoRepository;
import com.acadflow.backend.repository.CursoRepository;
import com.acadflow.backend.repository.UsuarioRepository;
import com.acadflow.backend.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/progresso")
@RequiredArgsConstructor
public class ProgressoController {

    private final UsuarioRepository usuarioRepository;
    private final CertificadoRepository certificadoRepository;
    private final CursoRepository cursoRepository;

    @GetMapping("/curso/{cursoId}")
    public List<ProgressoAlunoDTO> progressoPorCurso(@PathVariable String cursoId) {
        var curso = cursoRepository.findById(cursoId);
        if (curso.isEmpty()) return List.of();

        Integer horasExigidas = curso.get().getHorasExigidas();

        return usuarioRepository.findByRole(Role.ALUNO)
                .stream()
                .map(aluno -> {
                    int horasAcumuladas = certificadoRepository
                            .findByAlunoId(aluno.getId())
                            .stream()
                            .filter(c -> c.getStatus() == StatusCertificado.APROVADO)
                            .filter(c -> c.getCurso().getId().equals(cursoId))
                            .mapToInt(c -> c.getHoras() != null ? c.getHoras() : 0)
                            .sum();

                    double percentual = horasExigidas > 0
                            ? (double) horasAcumuladas / horasExigidas * 100
                            : 0;

                    String status = percentual >= 100 ? "Concluído" : "Em Andamento";

                    return new ProgressoAlunoDTO(
                            aluno.getId(),
                            aluno.getNome(),
                            horasAcumuladas,
                            horasExigidas,
                            Math.round(percentual * 100.0) / 100.0,
                            status
                    );
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<ProgressoAlunoDTO> progressoPorAluno(@PathVariable String alunoId) {
        return usuarioRepository.findById(alunoId)
                .map(aluno -> {
                    var certificados = certificadoRepository.findByAlunoId(alunoId)
                            .stream()
                            .filter(c -> c.getStatus() == StatusCertificado.APROVADO)
                            .collect(Collectors.toList());

                    int horasAcumuladas = certificados.stream()
                            .mapToInt(c -> c.getHoras() != null ? c.getHoras() : 0)
                            .sum();

                    int horasExigidas = aluno.getCurso() != null
                            ? aluno.getCurso().getHorasExigidas()
                            : 0;

                    double percentual = horasExigidas > 0
                            ? (double) horasAcumuladas / horasExigidas * 100
                            : 0;

                    String status = percentual >= 100 ? "Concluído" : "Em Andamento";

                    return ResponseEntity.ok(new ProgressoAlunoDTO(
                            aluno.getId(),
                            aluno.getNome(),
                            horasAcumuladas,
                            horasExigidas,
                            Math.round(percentual * 100.0) / 100.0,
                            status
                    ));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}