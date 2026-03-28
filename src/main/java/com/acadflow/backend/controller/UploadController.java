package com.acadflow.backend.controller;

import com.acadflow.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            String nomeArquivo = fileStorageService.salvarArquivo(file);
            return ResponseEntity.ok(Map.of(
                    "arquivo", nomeArquivo,
                    "url", "/uploads/" + nomeArquivo
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao fazer upload: " + e.getMessage());
        }
    }
}