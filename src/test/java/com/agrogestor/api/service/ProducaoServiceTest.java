package com.agrogestor.api.service;

import com.agrogestor.api.dto.ProducaoDTO;
import com.agrogestor.api.exception.NotFoundException;
import com.agrogestor.api.model.*;
import com.agrogestor.api.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProducaoServiceTest {
    ProducaoRepository producoes = mock(ProducaoRepository.class);
    AnimalRepository animais = mock(AnimalRepository.class);
    ProducaoService service = new ProducaoService(producoes, animais);
    Usuario usuario;
    Animal animal;
    ProducaoDTO.Entrada entrada = new ProducaoDTO.Entrada(3L, LocalDate.now(), Producao.Turno.MANHA, new BigDecimal("12.50"));
    @BeforeEach void iniciar() {
        usuario = new Usuario("Ana", "ana@example.com", "hash");
        ReflectionTestUtils.setField(usuario, "id", 1L);
        animal = new Animal(usuario, "A1", "Mimosa", Categoria.VACA_EM_LACTACAO, 12.0);
        ReflectionTestUtils.setField(animal, "id", 3L);
    }
    @Test void criaProducaoParaAnimalDoUsuario() {
        when(animais.findByIdAndUsuarioId(3L, 1L)).thenReturn(Optional.of(animal));
        when(producoes.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));
        assertThat(service.criar(usuario, entrada).litros()).isEqualByComparingTo("12.50");
    }
    @Test void naoPermiteAssociarAnimalDeOutroUsuario() {
        assertThatThrownBy(() -> service.criar(usuario, entrada)).isInstanceOf(NotFoundException.class);
        verify(producoes, never()).saveAndFlush(any());
    }
    @Test void naoAlteraNemExcluiProducaoDeOutroUsuario() {
        assertThatThrownBy(() -> service.atualizar(usuario, 42L, entrada)).isInstanceOf(NotFoundException.class);
        assertThatThrownBy(() -> service.excluir(usuario, 42L)).isInstanceOf(NotFoundException.class);
        verify(producoes, never()).delete(any());
    }
    @Test void rejeitaFiltrosEIntervalosInvalidos() {
        assertThatThrownBy(() -> service.listar(usuario, LocalDate.of(2025, 2, 1), LocalDate.of(2025, 1, 1), null, null, 0, 20)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.listar(usuario, null, null, null, null, 0, 101)).isInstanceOf(IllegalArgumentException.class);
    }
}
