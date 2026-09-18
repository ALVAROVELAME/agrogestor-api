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
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));

        animal.setBrinco(dto.getBrinco().trim());
        animal.setNome(dto.getNome().trim());
        animal.setCategoria(parseCategoria(dto.getCategoria()));
        animal.setProducaoDiaria(dto.getProducaoDiaria());

        return repo.save(animal);
    }

    public void excluir(Usuario usuario, Long id) {
        Animal animal = repo.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));
        repo.delete(animal);
    }

    private Categoria parseCategoria(String valor) {
        try {
            return Categoria.valueOf(valor.toUpperCase().replace(' ', '_'));
        } catch (Exception e) {
            // aceita tanto "Vaca em Lactação" quanto "VACA_EM_LACTACAO"
            return switch (valor) {
                case "Bezerra" -> Categoria.BEZERRA;
                case "Novilha" -> Categoria.NOVILHA;
                case "Vaca em Lactação" -> Categoria.VACA_EM_LACTACAO;
                case "Vaca Seca" -> Categoria.VACA_SECA;
                default -> throw new RuntimeException("Categoria inválida: " + valor);
            };
        }
    }
}