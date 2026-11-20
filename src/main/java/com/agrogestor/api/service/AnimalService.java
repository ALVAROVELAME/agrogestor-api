package com.agrogestor.api.service;

import com.agrogestor.api.dto.AnimalRequestDTO;
import com.agrogestor.api.model.Animal;
import com.agrogestor.api.model.Categoria;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.repository.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository repo;

    public AnimalService(AnimalRepository repo) {
        this.repo = repo;
    }

    public List<Animal> listarDoUsuario(Usuario usuario) {
        return repo.findByUsuarioIdOrderByCriadoEmDesc(usuario.getId());
    }

    public Animal buscar(Usuario usuario, Long id) {
        return repo.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new com.agrogestor.api.exception.NotFoundException("Animal não encontrado"));
    }

    @org.springframework.transaction.annotation.Transactional
    public Animal atualizarFicha(Usuario usuario, Long id, com.agrogestor.api.dto.FichaAnimalDTO dados) {
        Animal animal = buscar(usuario, id);
        if (dados.dataNascimento() != null) animal.setDataNascimento(dados.dataNascimento());
        if (dados.raca() != null) animal.setRaca(dados.raca().isBlank() ? null : dados.raca().trim());
        if (dados.origem() != null) animal.setOrigem(dados.origem().isBlank() ? null : dados.origem().trim());
        if (dados.observacoes() != null) animal.setObservacoes(dados.observacoes().isBlank() ? null : dados.observacoes().trim());
        return repo.save(animal);
    }

    public Animal criar(Usuario usuario, AnimalRequestDTO dto) {
        Categoria cat = parseCategoria(dto.getCategoria());

        Animal animal = new Animal(
                usuario,
                dto.getBrinco().trim(),
                dto.getNome().trim(),
                cat,
                dto.getProducaoDiaria()
        );

        return repo.save(animal);
    }

    public Animal atualizar(Usuario usuario, Long id, AnimalRequestDTO dto) {
        Animal animal = repo.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new com.agrogestor.api.exception.NotFoundException("Animal não encontrado"));

        animal.setBrinco(dto.getBrinco().trim());
        animal.setNome(dto.getNome().trim());
        animal.setCategoria(parseCategoria(dto.getCategoria()));
        animal.setProducaoDiaria(dto.getProducaoDiaria());

        return repo.save(animal);
    }

    public void excluir(Usuario usuario, Long id) {
        Animal animal = repo.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new com.agrogestor.api.exception.NotFoundException("Animal não encontrado"));
        repo.delete(animal);
    }

    private Categoria parseCategoria(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        String normalizada = java.text.Normalizer.normalize(valor.trim(), java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").toUpperCase(java.util.Locale.ROOT).replaceAll("\\s+", "_");
        try {
            return Categoria.valueOf(normalizada);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Categoria inválida");
        }
    }
}
