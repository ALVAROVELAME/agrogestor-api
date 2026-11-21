package com.agrogestor.api.service.pecuaria;

import com.agrogestor.api.dto.animal.AnimalRequestDTO;
import com.agrogestor.api.dto.animal.FichaAnimalDTO;
import java.time.LocalDate;
import com.agrogestor.api.model.*;
import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.repository.AnimalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {
    @Mock AnimalRepository repo;
    @InjectMocks AnimalService service;
    Usuario usuario;
    AnimalRequestDTO dto;

    @BeforeEach void preparar() {
        usuario = new Usuario("Maria", "maria@example.com", "hash");
        ReflectionTestUtils.setField(usuario, "id", 7L);
        dto = new AnimalRequestDTO();
        dto.setBrinco(" A-1 "); dto.setNome(" Mimosa ");
        dto.setCategoria("BEZERRA"); dto.setProducaoDiaria(0.0);
    }

    @Test void listaSomenteAnimaisDoUsuario() {
        when(repo.findByUsuarioIdOrderByCriadoEmDesc(7L)).thenReturn(List.of());
        assertThat(service.listarDoUsuario(usuario)).isEmpty();
        verify(repo).findByUsuarioIdOrderByCriadoEmDesc(7L);
    }

    @ParameterizedTest
    @ValueSource(strings = {"VACA_EM_LACTACAO", "Vaca em Lactação", " vaca em lactação "})
    void criaNormalizandoCategoriaECampos(String categoria) {
        dto.setCategoria(categoria);
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        Animal animal = service.criar(usuario, dto);
        assertThat(animal.getUsuario()).isSameAs(usuario);
        assertThat(animal.getBrinco()).isEqualTo("A-1");
        assertThat(animal.getNome()).isEqualTo("Mimosa");
        assertThat(animal.getCategoria()).isEqualTo(Categoria.VACA_EM_LACTACAO);
    }

    @Test void rejeitaCategoriaInvalidaSemSalvar() {
        dto.setCategoria("INVALIDA");
        assertThatThrownBy(() -> service.criar(usuario, dto)).isInstanceOf(IllegalArgumentException.class);
        verify(repo, never()).save(any());
    }

    @Test void atualizaAnimalDoUsuario() {
        Animal animal = new Animal(usuario, "antigo", "antigo", Categoria.NOVILHA, 0.0);
        when(repo.findByIdAndUsuarioId(10L, 7L)).thenReturn(Optional.of(animal));
        when(repo.save(animal)).thenReturn(animal);
        assertThat(service.atualizar(usuario, 10L, dto).getNome()).isEqualTo("Mimosa");
        verify(repo).save(animal);
    }

    @Test void naoAtualizaAnimalDeOutroUsuario() {
        when(repo.findByIdAndUsuarioId(10L, 7L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.atualizar(usuario, 10L, dto)).hasMessage("Animal não encontrado");
        verify(repo, never()).save(any());
    }

    @Test void naoExcluiAnimalDeOutroUsuario() {
        when(repo.findByIdAndUsuarioId(10L, 7L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.excluir(usuario, 10L)).hasMessage("Animal não encontrado");
        verify(repo, never()).delete(any());
    }

    @Test void excluiAnimalDoUsuario() {
        Animal animal = new Animal(usuario, "A", "Mimosa", Categoria.BEZERRA, 0.0);
        when(repo.findByIdAndUsuarioId(10L, 7L)).thenReturn(Optional.of(animal));
        service.excluir(usuario, 10L);
        verify(repo).delete(animal);
    }

    @Test void atualizaFichaPreservandoAusentesELimpandoTextoVazio() {
        Animal animal = new Animal(usuario, "A", "Mimosa", Categoria.BEZERRA, 0.0);
        animal.setOrigem("Fazenda"); animal.setObservacoes("antiga");
        when(repo.findByIdAndUsuarioId(10L, 7L)).thenReturn(Optional.of(animal));
        when(repo.save(animal)).thenReturn(animal);
        var nascimento = LocalDate.of(2024, 1, 1);
        var resultado = service.atualizarFicha(usuario, 10L, new FichaAnimalDTO(nascimento, "  Jersey  ", null, "  "));
        assertThat(resultado.getDataNascimento()).isEqualTo(nascimento);
        assertThat(resultado.getRaca()).isEqualTo("Jersey");
        assertThat(resultado.getOrigem()).isEqualTo("Fazenda");
        assertThat(resultado.getObservacoes()).isNull();
    }
}
