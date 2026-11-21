package com.agrogestor.api.controller;

import com.agrogestor.api.dto.GestaoDTO;
import com.agrogestor.api.model.RegistroGestao;
import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.service.GestaoService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GestaoController {
    private final GestaoService service;
    public GestaoController(GestaoService service) { this.service = service; }

    @GetMapping("/tarefas")
    public Page<GestaoDTO.Saida> tarefas(@AuthenticationPrincipal Usuario usuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) Long animalId, @RequestParam(required = false) Boolean concluida,
            @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "20") int tamanho) {
        return service.listar(usuario, RegistroGestao.Tipo.TAREFA, inicio, fim, animalId, concluida, null, pagina, tamanho);
    }
    @PostMapping("/tarefas")
    public ResponseEntity<GestaoDTO.Saida> criarTarefa(@AuthenticationPrincipal Usuario usuario, @Valid @RequestBody GestaoDTO.Tarefa dados) {
        return ResponseEntity.status(201).body(service.criarTarefa(usuario, dados));
    }
    @PatchMapping("/tarefas/{id}")
    public GestaoDTO.Saida atualizarTarefa(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id, @Valid @RequestBody GestaoDTO.AtualizarTarefa dados) {
        return service.atualizarTarefa(usuario, id, dados);
    }
    @DeleteMapping("/tarefas/{id}")
    public ResponseEntity<Void> excluirTarefa(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id) {
        service.excluir(usuario, id, RegistroGestao.Tipo.TAREFA); return ResponseEntity.noContent().build();
    }

    @GetMapping("/eventos")
    public Page<GestaoDTO.Saida> eventos(@AuthenticationPrincipal Usuario usuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) Long animalId, @RequestParam(required = false) RegistroGestao.TipoEvento tipo,
            @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "20") int tamanho) {
        return service.listar(usuario, RegistroGestao.Tipo.EVENTO, inicio, fim, animalId, null, tipo == null ? null : tipo.name(), pagina, tamanho);
    }
    @PostMapping("/eventos")
    public ResponseEntity<GestaoDTO.Saida> criarEvento(@AuthenticationPrincipal Usuario usuario, @Valid @RequestBody GestaoDTO.Evento dados) {
        return ResponseEntity.status(201).body(service.salvarEvento(usuario, null, dados));
    }
    @PutMapping("/eventos/{id}")
    public GestaoDTO.Saida atualizarEvento(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id, @Valid @RequestBody GestaoDTO.Evento dados) {
        return service.salvarEvento(usuario, id, dados);
    }
    @DeleteMapping("/eventos/{id}")
    public ResponseEntity<Void> excluirEvento(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id) {
        service.excluir(usuario, id, RegistroGestao.Tipo.EVENTO); return ResponseEntity.noContent().build();
    }

    @GetMapping("/financeiro/lancamentos")
    public Page<GestaoDTO.Saida> financeiro(@AuthenticationPrincipal Usuario usuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) RegistroGestao.TipoFinanceiro tipo,
            @RequestParam(defaultValue = "0") int pagina, @RequestParam(defaultValue = "20") int tamanho) {
        return service.listar(usuario, RegistroGestao.Tipo.FINANCEIRO, inicio, fim, null, null, tipo == null ? null : tipo.name(), pagina, tamanho);
    }
    @PostMapping("/financeiro/lancamentos")
    public ResponseEntity<GestaoDTO.Saida> criarFinanceiro(@AuthenticationPrincipal Usuario usuario, @Valid @RequestBody GestaoDTO.Financeiro dados) {
        return ResponseEntity.status(201).body(service.salvarFinanceiro(usuario, null, dados));
    }
    @PutMapping("/financeiro/lancamentos/{id}")
    public GestaoDTO.Saida atualizarFinanceiro(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id, @Valid @RequestBody GestaoDTO.Financeiro dados) {
        return service.salvarFinanceiro(usuario, id, dados);
    }
    @DeleteMapping("/financeiro/lancamentos/{id}")
    public ResponseEntity<Void> excluirFinanceiro(@AuthenticationPrincipal Usuario usuario, @PathVariable Long id) {
        service.excluir(usuario, id, RegistroGestao.Tipo.FINANCEIRO); return ResponseEntity.noContent().build();
    }
}
