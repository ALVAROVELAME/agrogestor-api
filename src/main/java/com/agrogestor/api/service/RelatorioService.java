package com.agrogestor.api.service;

import com.agrogestor.api.model.RegistroGestao;
import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.repository.ProducaoRepository;
import com.agrogestor.api.repository.RegistroGestaoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RelatorioService {
    public record ProducaoDia(LocalDate data, BigDecimal litros, long registros) {}
    public record Financeiro(BigDecimal receitas, BigDecimal despesas, BigDecimal saldo) {}
    public record EventoTipo(String tipo, long quantidade) {}
    public record Indicadores(LocalDate inicio, LocalDate fim, BigDecimal litros, Financeiro financeiro, long tarefasPendentes, long tarefasAtrasadas) {}
    private final ProducaoRepository producoes;
    private final RegistroGestaoRepository registros;
    public RelatorioService(ProducaoRepository producoes, RegistroGestaoRepository registros) { this.producoes = producoes; this.registros = registros; }

    private void validar(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null || inicio.isAfter(fim) || ChronoUnit.DAYS.between(inicio, fim) > 365)
            throw new IllegalArgumentException("Informe um período válido de até 366 dias");
    }
    @Transactional(readOnly = true)
    public List<ProducaoDia> producao(Usuario usuario, LocalDate inicio, LocalDate fim, Long animalId) {
        validar(inicio, fim);
        return producoes.totalPorDia(usuario.getId(), inicio, fim, animalId).stream()
                .map(row -> new ProducaoDia((LocalDate) row[0], (BigDecimal) row[1], ((Number) row[2]).longValue())).toList();
    }
    @Transactional(readOnly = true)
    public Financeiro financeiro(Usuario usuario, LocalDate inicio, LocalDate fim) {
        validar(inicio, fim);
        BigDecimal receitas = BigDecimal.ZERO, despesas = BigDecimal.ZERO;
        for (Object[] row : registros.somarPorSubtipo(usuario.getId(), RegistroGestao.Tipo.FINANCEIRO, inicio, fim)) {
            if ("RECEITA".equals(row[0])) receitas = (BigDecimal) row[1];
            if ("DESPESA".equals(row[0])) despesas = (BigDecimal) row[1];
        }
        return new Financeiro(receitas, despesas, receitas.subtract(despesas));
    }
    @Transactional(readOnly = true)
    public List<EventoTipo> eventos(Usuario usuario, LocalDate inicio, LocalDate fim) {
        validar(inicio, fim);
        return registros.contarPorSubtipo(usuario.getId(), RegistroGestao.Tipo.EVENTO, inicio, fim).stream()
                .map(row -> new EventoTipo((String) row[0], ((Number) row[1]).longValue())).toList();
    }
    @Transactional(readOnly = true)
    public Indicadores indicadores(Usuario usuario, LocalDate inicio, LocalDate fim, LocalDate referencia) {
        validar(inicio, fim);
        BigDecimal litros = producao(usuario, inicio, fim, null).stream().map(ProducaoDia::litros).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Indicadores(inicio, fim, litros, financeiro(usuario, inicio, fim),
                registros.countByUsuarioIdAndTipoAndConcluida(usuario.getId(), RegistroGestao.Tipo.TAREFA, false),
                registros.countByUsuarioIdAndTipoAndConcluidaAndDataLessThan(usuario.getId(), RegistroGestao.Tipo.TAREFA, false, referencia));
    }
}
