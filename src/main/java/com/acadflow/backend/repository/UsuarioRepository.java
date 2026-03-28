package com.acadflow.backend.repository;
import com.acadflow.backend.entity.Usuario;
import com.acadflow.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRole(Role role);
    List<Usuario> findByCursoId(String cursoId);
    boolean existsByEmail(String email);
}
