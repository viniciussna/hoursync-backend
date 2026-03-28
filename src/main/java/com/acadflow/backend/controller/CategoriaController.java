package com.acadflow.backend.controller;
import com.acadflow.backend.entity.Categoria;
import com.acadflow.backend.repository.CategoriaRepository;
import com.acadflow.backend.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaRepository categoriaRepository;
    private final CursoRepository cursoRepository;

    @GetMapping
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @GetMapping("/curso/{cursoId}")
    public List<Categoria> listarPorCurso(@PathVariable String cursoId) {
        return categoriaRepository.findByCursoId(cursoId);
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria) {
        if (categoria.getCurso() == null || categoria.getCurso().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return cursoRepository.findById(categoria.getCurso().getId())
                .map(curso -> {
                    categoria.setCurso(curso);
                    return ResponseEntity.ok(categoriaRepository.save(categoria));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        if (!categoriaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
