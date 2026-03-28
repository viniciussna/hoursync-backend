package com.acadflow.backend.controller;
import com.acadflow.backend.entity.Certificado;
import com.acadflow.backend.entity.StatusCertificado;
import com.acadflow.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.acadflow.backend.service.EmailService;

import java.util.List;

@RestController
@RequestMapping("/certificados")
@RequiredArgsConstructor
public class CertificadoController {

    private final CertificadoRepository certificadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final CategoriaRepository categoriaRepository;
    private final EmailService emailService;

    @GetMapping
    public List<Certificado> listar() {
        return certificadoRepository.findAll();
    }

    @GetMapping("/aluno/{alunoId}")
    public List<Certificado> listarPorAluno(@PathVariable String alunoId) {
        return certificadoRepository.findByAlunoId(alunoId);
    }

    @GetMapping("/curso/{cursoId}")
    public List<Certificado> listarPorCurso(@PathVariable String cursoId) {
        return certificadoRepository.findByCursoId(cursoId);
    }

    @GetMapping("/status/{status}")
    public List<Certificado> listarPorStatus(@PathVariable StatusCertificado status) {
        return certificadoRepository.findByStatus(status);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Certificado certificado) {
        if (certificado.getAluno() == null || certificado.getAluno().getId() == null)
            return ResponseEntity.badRequest().body("Aluno obrigatório");
        if (certificado.getCategoria() == null || certificado.getCategoria().getId() == null)
            return ResponseEntity.badRequest().body("Categoria obrigatória");
        if (certificado.getCurso() == null || certificado.getCurso().getId() == null)
            return ResponseEntity.badRequest().body("Curso obrigatório");

        var aluno = usuarioRepository.findById(certificado.getAluno().getId());
        var categoria = categoriaRepository.findById(certificado.getCategoria().getId());
        var curso = cursoRepository.findById(certificado.getCurso().getId());

        if (aluno.isEmpty()) return ResponseEntity.badRequest().body("Aluno não encontrado");
        if (categoria.isEmpty()) return ResponseEntity.badRequest().body("Categoria não encontrada");
        if (curso.isEmpty()) return ResponseEntity.badRequest().body("Curso não encontrado");

        certificado.setAluno(aluno.get());
        certificado.setCategoria(categoria.get());
        certificado.setCurso(curso.get());
        certificado.setStatus(StatusCertificado.PENDENTE);

        return ResponseEntity.ok(certificadoRepository.save(certificado));
    }

    @PatchMapping("/{id}/validar")
    public ResponseEntity<?> validar(
            @PathVariable String id,
            @RequestParam StatusCertificado status,
            @RequestParam(required = false) String justificativa,
            @RequestParam String coordenadorId) {

        return certificadoRepository.findById(id)
                .map(cert -> {
                    cert.setStatus(status);
                    cert.setJustificativa(justificativa);
                    usuarioRepository.findById(coordenadorId)
                            .ifPresent(cert::setCoordenador);
                    Certificado salvo = certificadoRepository.save(cert);

                    String emailAluno = cert.getAluno().getEmail();
                    String nomeAluno = cert.getAluno().getNome();
                    String titulo = cert.getTitulo();

                    if (status == StatusCertificado.APROVADO) {
                        emailService.enviarEmailAprovacao(emailAluno, nomeAluno, titulo);
                    } else if (status == StatusCertificado.REJEITADO) {
                        emailService.enviarEmailRejeicao(emailAluno, nomeAluno, titulo, justificativa);
                    }

                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certificado> buscarPorId(@PathVariable String id) {
        return certificadoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
