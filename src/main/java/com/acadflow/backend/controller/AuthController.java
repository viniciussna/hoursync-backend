package com.acadflow.backend.controller;

import com.acadflow.backend.dto.LoginRequest;
import com.acadflow.backend.dto.LoginResponse;
import com.acadflow.backend.repository.UsuarioRepository;
import com.acadflow.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return usuarioRepository.findByEmail(request.getEmail())
                .map(usuario -> {
                    if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
                        return ResponseEntity.status(401).body("Senha incorreta");
                    }
                    if (!usuario.getAtivo()) {
                        return ResponseEntity.status(403).body("Usuário inativo");
                    }
                    String token = jwtUtil.gerarToken(
                            usuario.getEmail(),
                            usuario.getRole().name()
                    );
                    return ResponseEntity.ok(new LoginResponse(
                            token,
                            usuario.getRole().name(),
                            usuario.getNome(),
                            usuario.getId()
                    ));
                })
                .orElse(ResponseEntity.status(404).body("Usuário não encontrado"));
    }


}