package com.agrogestor.api.controller;

import com.agrogestor.api.dto.NomeRequestDTO;
import com.agrogestor.api.model.Nome;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.service.NomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nomes")
public class NomeController {

    private final NomeService service;

    public NomeController(NomeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Nome> criar(
            @AuthenticationPrincipal Usuario usuario,
            @RequestBody NomeRequestDTO dto
    ) {
        return ResponseEntity.ok(service.salvar(usuario, dto.getNome()));
    }

    @GetMapping
    public ResponseEntity<List<Nome>> listar(
            @AuthenticationPrincipal Usuario usuario
    ) {
        return ResponseEntity.ok(service.listar(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long id
    ) {
        service.deletar(usuario, id);
        return ResponseEntity.noContent().build();
    }
}