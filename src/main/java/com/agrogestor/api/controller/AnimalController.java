package com.agrogestor.api.controller;

import com.agrogestor.api.dto.AnimalRequestDTO;
import com.agrogestor.api.dto.AnimalRespostaDTO;
import com.agrogestor.api.model.Animal;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private final AnimalService service;

    public AnimalController(AnimalService service) {
        this.service = service;
    }

    /** Lista apenas os animais do usuário logado. */
    @GetMapping
    public ResponseEntity<List<AnimalRespostaDTO>> listar(
            @AuthenticationPrincipal Usuario usuario
    ) {
        List<AnimalRespostaDTO> lista = service.listarDoUsuario(usuario)
                .stream()
                .map(AnimalRespostaDTO::new)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<AnimalRespostaDTO> criar(
            @AuthenticationPrincipal Usuario usuario,
            @Valid @RequestBody AnimalRequestDTO dto
    ) {
        Animal animal = service.criar(usuario, dto);
        return ResponseEntity.ok(new AnimalRespostaDTO(animal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalRespostaDTO> atualizar(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long id,
            @Valid @RequestBody AnimalRequestDTO dto
    ) {
        Animal animal = service.atualizar(usuario, id, dto);
        return ResponseEntity.ok(new AnimalRespostaDTO(animal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @AuthenticationPrincipal Usuario usuario,
            @PathVariable Long id
    ) {
        service.excluir(usuario, id);
        return ResponseEntity.noContent().build();
    }
}