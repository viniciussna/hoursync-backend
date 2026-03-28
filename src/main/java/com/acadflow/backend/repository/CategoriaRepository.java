package com.acadflow.backend.repository;

import com.acadflow.backend.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, String> {
    List<Categoria> findByCursoId(String cursoId);
}
