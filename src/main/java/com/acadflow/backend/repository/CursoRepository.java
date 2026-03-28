package com.acadflow.backend.repository;

import com.acadflow.backend.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, String> {
}
