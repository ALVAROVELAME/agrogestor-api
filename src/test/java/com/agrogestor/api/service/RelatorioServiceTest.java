package com.agrogestor.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.agrogestor.api.model.RegistroGestao;
import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.repository.ProducaoRepository;
import com.agrogestor.api.repository.RegistroGestaoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class RelatorioServiceTest {
    private final ProducaoRepository producoes = mock(ProducaoRepository.class);
    private final RegistroGestaoRepository registros = mock(RegistroGestaoRepository.class);
    private final RelatorioService service = new RelatorioService(producoes, registros);
    private final Usuario usuario = mock(Usuario.class);
    private final LocalDate inicio = LocalDate.of(2026, 1, 1);
    private final LocalDate fim = LocalDate.of(2026, 1, 31);

    @Test
    void calculaFinanceiroUsandoSomenteUsuarioAutenticado() {
        when(usuario.getId()).thenReturn(7L);
        when(registros.somarPorSubtipo(7L, RegistroGestao.Tipo.FINANCEIRO, inicio, fim)).thenReturn(List.of(
                new Object[]{"RECEITA", new BigDecimal("120.50")}, new Object[]{"DESPESA", new BigDecimal("20.25")}));
        var resultado = service.financeiro(usuario, inicio, fim);
        assertEquals(new BigDecimal("100.25"), resultado.saldo());
        verify(registros).somarPorSubtipo(7L, RegistroGestao.Tipo.FINANCEIRO, inicio, fim);
    }

    @Test
    void recusaPeriodosInvalidosSemConsultarBanco() {
        assertThrows(IllegalArgumentException.class, () -> service.producao(usuario, fim, inicio, null));
        assertThrows(IllegalArgumentException.class, () -> service.financeiro(usuario, inicio, inicio.plusDays(366)));
        verifyNoInteractions(producoes, registros);
    }

    @Test
    void indicadoresCombinamProducaoFinanceiroETarefas() {
        when(usuario.getId()).thenReturn(7L);
        when(producoes.totalPorDia(7L, inicio, fim, null)).thenReturn(java.util.Collections.singletonList(new Object[]{inicio, new BigDecimal("42.50"), 2L}));
        when(registros.somarPorSubtipo(7L, RegistroGestao.Tipo.FINANCEIRO, inicio, fim)).thenReturn(java.util.Collections.singletonList(new Object[]{"DESPESA", new BigDecimal("10.00")}));
        when(registros.countByUsuarioIdAndTipoAndConcluida(7L, RegistroGestao.Tipo.TAREFA, false)).thenReturn(3L);
        when(registros.countByUsuarioIdAndTipoAndConcluidaAndDataLessThan(7L, RegistroGestao.Tipo.TAREFA, false, fim)).thenReturn(1L);
        var resultado = service.indicadores(usuario, inicio, fim, fim);
        assertEquals(new BigDecimal("42.50"), resultado.litros());
        assertEquals(new BigDecimal("-10.00"), resultado.financeiro().saldo());
        assertEquals(3, resultado.tarefasPendentes());
        assertEquals(1, resultado.tarefasAtrasadas());
    }

    @Test
    void agrupaEventosDoUsuarioETrataFinanceiroSemMovimentos() {
        when(usuario.getId()).thenReturn(7L);
        when(registros.contarPorSubtipo(7L, RegistroGestao.Tipo.EVENTO, inicio, fim))
                .thenReturn(java.util.Collections.singletonList(new Object[]{"VACINACAO", 2L}));
        assertEquals(List.of(new RelatorioService.EventoTipo("VACINACAO", 2)), service.eventos(usuario, inicio, fim));
        assertEquals(BigDecimal.ZERO, service.financeiro(usuario, inicio, fim).saldo());
        assertThrows(IllegalArgumentException.class, () -> service.eventos(usuario, null, fim));
    }
}
