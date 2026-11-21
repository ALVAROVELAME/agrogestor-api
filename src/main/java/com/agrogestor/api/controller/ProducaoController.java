package com.agrogestor.api.controller;

import com.agrogestor.api.dto.ProducaoDTO;
import com.agrogestor.api.model.Producao;
import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.service.pecuaria.ProducaoService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/producoes")
public class ProducaoController {
    private final ProducaoService service;
    public ProducaoController(ProducaoService service) { this.service = service; }
    @GetMapping
    public Page<ProducaoDTO.Saida> listar(@AuthenticationPrincipal Usuario usuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) Long animalId, @RequestParam(required = false) Producao.Turno turno,
            @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "20") int tamanho) {
        return service.listar(usuario, inicio, fim, animalId, turno, pagina, tamanho);
    }
    @PostMapping
    public ResponseEntity<ProducaoDTO.Saida> criar(@AuthenticationPrincipal Usuario usuario, @Valid @RequestBody ProducaoDTO.Entrada dados) {
        return ResponseEntity.status(201).body(service.criar(usuario, dados));
    }
    @PutMapping("/{id}")
    public ProducaoDTO.Saida atualizar(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id, @Valid @RequestBody ProducaoDTO.Entrada dados) {
        return service.atualizar(usuario, id, dados);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id) {
        service.excluir(usuario, id); return ResponseEntity.noContent().build();
    }
}
