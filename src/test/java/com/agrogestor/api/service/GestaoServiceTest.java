package com.agrogestor.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.agrogestor.api.dto.GestaoDTO;
import com.agrogestor.api.exception.NotFoundException;
import com.agrogestor.api.model.RegistroGestao;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.repository.AnimalRepository;
import com.agrogestor.api.repository.RegistroGestaoRepository;
import java.time.LocalDate;
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
}
