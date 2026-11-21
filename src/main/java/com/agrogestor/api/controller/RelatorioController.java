package com.agrogestor.api.controller;

import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.service.RelatorioService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RelatorioController {
    public enum Exportacao { PRODUCAO, FINANCEIRO, EVENTOS }
    private final RelatorioService service;
    public RelatorioController(RelatorioService service) { this.service = service; }

    @GetMapping("/relatorios/producao")
    public List<RelatorioService.ProducaoDia> producao(@AuthenticationPrincipal Usuario usuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) Long animalId) {
        return service.producao(usuario, inicio, fim, animalId);
    }
    @GetMapping("/relatorios/financeiro")
    public RelatorioService.Financeiro financeiro(@AuthenticationPrincipal Usuario usuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.financeiro(usuario, inicio, fim);
    }
    @GetMapping("/relatorios/eventos")
    public List<RelatorioService.EventoTipo> eventos(@AuthenticationPrincipal Usuario usuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return service.eventos(usuario, inicio, fim);
    }
    @GetMapping("/indicadores")
    public RelatorioService.Indicadores indicadores(@AuthenticationPrincipal Usuario usuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate referencia) {
        return service.indicadores(usuario, inicio, fim, referencia);
    }
    @GetMapping(value = "/relatorios/exportacao", produces = "text/csv")
    public ResponseEntity<String> exportar(@AuthenticationPrincipal Usuario usuario, @RequestParam Exportacao tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        StringBuilder csv = new StringBuilder();
        switch (tipo) {
            case PRODUCAO -> {
                csv.append("data,litros,registros\r\n");
                service.producao(usuario, inicio, fim, null).forEach(d -> csv.append(d.data()).append(',').append(d.litros()).append(',').append(d.registros()).append("\r\n"));
            }
            case FINANCEIRO -> {
                var f = service.financeiro(usuario, inicio, fim);
                csv.append("receitas,despesas,saldo\r\n").append(f.receitas()).append(',').append(f.despesas()).append(',').append(f.saldo()).append("\r\n");
            }
            case EVENTOS -> {
                csv.append("tipo,quantidade\r\n");
                service.eventos(usuario, inicio, fim).forEach(e -> csv.append(e.tipo()).append(',').append(e.quantidade()).append("\r\n"));
            }
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio.csv\"")
                .contentType(new MediaType("text", "csv", java.nio.charset.StandardCharsets.UTF_8)).body(csv.toString());
    }
}
