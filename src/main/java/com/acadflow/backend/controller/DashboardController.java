package com.acadflow.backend.controller;
import com.acadflow.backend.dto.DashboardDTO;
import com.acadflow.backend.entity.Role;
import com.acadflow.backend.entity.StatusCertificado;
import com.acadflow.backend.repository.CertificadoRepository;
import com.acadflow.backend.repository.CursoRepository;
import com.acadflow.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CertificadoRepository certificadoRepository;

    @GetMapping("/admin")
    public DashboardDTO dashboardAdmin() {
        long totalCursos = cursoRepository.count();
        long totalCoordenadores = usuarioRepository.findByRole(Role.COORDENADOR).size();
        long totalAlunos = usuarioRepository.findByRole(Role.ALUNO).size();
        long totalCertificados = certificadoRepository.count();
        long pendentes = certificadoRepository.countByStatus(StatusCertificado.PENDENTE);
        long aprovados = certificadoRepository.countByStatus(StatusCertificado.APROVADO);
        long rejeitados = certificadoRepository.countByStatus(StatusCertificado.REJEITADO);

        long horasValidadas = certificadoRepository.findByStatus(StatusCertificado.APROVADO)
                .stream()
                .mapToLong(c -> c.getHoras() != null ? c.getHoras() : 0)
                .sum();

        double taxa = totalCertificados > 0
                ? (double) aprovados / totalCertificados * 100
                : 0;

        return new DashboardDTO(
                totalCursos,
                totalCoordenadores,
                totalAlunos,
                totalCertificados,
                pendentes,
                aprovados,
                rejeitados,
                horasValidadas,
                Math.round(taxa * 100.0) / 100.0
        );
    }

    @GetMapping("/coordenador/{cursoId}")
    public DashboardDTO dashboardCoordenador(@PathVariable String cursoId) {
        long pendentes = certificadoRepository.findByCursoIdAndStatus(cursoId, StatusCertificado.PENDENTE).size();
        long aprovados = certificadoRepository.findByCursoIdAndStatus(cursoId, StatusCertificado.APROVADO).size();
        long rejeitados = certificadoRepository.findByCursoIdAndStatus(cursoId, StatusCertificado.REJEITADO).size();
        long total = certificadoRepository.countByCursoId(cursoId);

        long horasValidadas = certificadoRepository.findByCursoIdAndStatus(cursoId, StatusCertificado.APROVADO)
                .stream()
                .mapToLong(c -> c.getHoras() != null ? c.getHoras() : 0)
                .sum();

        double taxa = total > 0 ? (double) aprovados / total * 100 : 0;

        return new DashboardDTO(
                0, 0, 0, total,
                pendentes, aprovados, rejeitados,
                horasValidadas,
                Math.round(taxa * 100.0) / 100.0
        );
    }
}
