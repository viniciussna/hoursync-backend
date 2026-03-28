package com.acadflow.backend.controller;

import com.acadflow.backend.entity.Role;
import com.acadflow.backend.entity.Usuario;
import com.acadflow.backend.repository.CursoRepository;
import com.acadflow.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/coordenadores")
    public List<Usuario> listarCoordenadores() {
        return usuarioRepository.findByRole(Role.COORDENADOR);
    }

    @GetMapping("/alunos")
    public List<Usuario> listarAlunos() {
        return usuarioRepository.findByRole(Role.ALUNO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable String id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }
        if (usuario.getCurso() != null && usuario.getCurso().getId() != null) {
            cursoRepository.findById(usuario.getCurso().getId())
                    .ifPresent(usuario::setCurso);
        }
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    @PutMapping("/{id}/ativo")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Usuario> alterarStatus(@PathVariable String id, @RequestParam Boolean ativo) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setAtivo(ativo);
                    return ResponseEntity.ok(usuarioRepository.save(usuario));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}