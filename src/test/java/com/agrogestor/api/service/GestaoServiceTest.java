package com.agrogestor.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.agrogestor.api.dto.GestaoDTO;
import com.agrogestor.api.exception.NotFoundException;
import com.agrogestor.api.model.RegistroGestao;
import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.repository.AnimalRepository;
import com.agrogestor.api.repository.RegistroGestaoRepository;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class GestaoServiceTest {
    private final RegistroGestaoRepository registros = mock(RegistroGestaoRepository.class);
    private final AnimalRepository animais = mock(AnimalRepository.class);
    private final GestaoService service = new GestaoService(registros, animais);
    private final Usuario usuario = mock(Usuario.class);

    @Test
    void impedeCriarTarefaComAnimalDeOutroUsuario() {
        when(usuario.getId()).thenReturn(5L);
        when(animais.findByIdAndUsuarioId(99L, 5L)).thenReturn(Optional.empty());
        var dados = new GestaoDTO.Tarefa("Vacinar", LocalDate.of(2026, 1, 1), 99L);
        assertThrows(NotFoundException.class, () -> service.criarTarefa(usuario, dados));
        verify(registros, never()).save(any());
    }

    @Test
    void impedeExcluirRegistroDeOutroUsuarioOuTipo() {
        when(usuario.getId()).thenReturn(5L);
        when(registros.findByIdAndUsuarioIdAndTipo(99L, 5L, RegistroGestao.Tipo.FINANCEIRO)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.excluir(usuario, 99L, RegistroGestao.Tipo.FINANCEIRO));
        verify(registros, never()).delete(any());
    }

    @Test
    void rejeitaPaginacaoEIntervaloInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> service.listar(usuario, RegistroGestao.Tipo.TAREFA, null, null, null, null, null, 0, 101));
        assertThrows(IllegalArgumentException.class, () -> service.listar(usuario, RegistroGestao.Tipo.TAREFA, LocalDate.of(2026, 2, 1), LocalDate.of(2026, 1, 1), null, null, null, 0, 20));
        verifyNoInteractions(registros);
    }

    @Test
    void criaEAtualizaTarefaSemPerderCamposNaoInformados() {
        when(usuario.getId()).thenReturn(5L);
        when(registros.save(any())).thenAnswer(inv -> inv.getArgument(0));
        var dia = LocalDate.of(2026, 1, 1);
        var criada = service.criarTarefa(usuario, new GestaoDTO.Tarefa("  Vacinar  ", dia, null));
        assertEquals("Vacinar", criada.descricao());
        assertFalse(criada.concluida());
        var registro = new RegistroGestao(usuario, RegistroGestao.Tipo.TAREFA);
        registro.setDescricao("Vacinar"); registro.setData(dia); registro.setConcluida(false);
        when(registros.findByIdAndUsuarioIdAndTipo(2L, 5L, RegistroGestao.Tipo.TAREFA)).thenReturn(Optional.of(registro));
        var atualizada = service.atualizarTarefa(usuario, 2L, new GestaoDTO.AtualizarTarefa(null, null, true));
        assertEquals("Vacinar", atualizada.descricao());
        assertEquals(dia, atualizada.data());
        assertTrue(atualizada.concluida());
        assertThrows(IllegalArgumentException.class, () -> service.atualizarTarefa(usuario, 2L, new GestaoDTO.AtualizarTarefa("  ", null, null)));
        verify(registros, times(2)).save(any());
    }

    @Test
    void criaEAtualizaRegistrosFinanceirosEEventosDoUsuario() {
        when(usuario.getId()).thenReturn(5L);
        var animal = mock(com.agrogestor.api.model.Animal.class);
        when(animal.getId()).thenReturn(3L);
        when(animal.getNome()).thenReturn("Mimosa");
        when(animais.findByIdAndUsuarioId(3L, 5L)).thenReturn(Optional.of(animal));
        when(registros.save(any())).thenAnswer(inv -> inv.getArgument(0));
        var dia = LocalDate.of(2026, 1, 1);
        var evento = new GestaoDTO.Evento(3L, dia, RegistroGestao.TipoEvento.VACINACAO, "  Vacina  ");
        var saida = service.salvarEvento(usuario, null, evento);
        assertEquals("VACINACAO", saida.subtipo());
        assertEquals("Vacina", saida.descricao());
        assertEquals(3L, saida.animalId());
        var existente = new RegistroGestao(usuario, RegistroGestao.Tipo.EVENTO);
        when(registros.findByIdAndUsuarioIdAndTipo(4L, 5L, RegistroGestao.Tipo.EVENTO)).thenReturn(Optional.of(existente));
        assertEquals("Mimosa", service.salvarEvento(usuario, 4L, evento).animalNome());
        var financeiro = new GestaoDTO.Financeiro(dia, RegistroGestao.TipoFinanceiro.DESPESA, "  Ração  ", new BigDecimal("12.50"));
        assertEquals(new BigDecimal("12.50"), service.salvarFinanceiro(usuario, null, financeiro).valor());
        var registro = new RegistroGestao(usuario, RegistroGestao.Tipo.FINANCEIRO);
        when(registros.findByIdAndUsuarioIdAndTipo(6L, 5L, RegistroGestao.Tipo.FINANCEIRO)).thenReturn(Optional.of(registro));
        assertEquals("Ração", service.salvarFinanceiro(usuario, 6L, financeiro).descricao());
        service.excluir(usuario, 6L, RegistroGestao.Tipo.FINANCEIRO);
        verify(registros).delete(registro);
    }
}
