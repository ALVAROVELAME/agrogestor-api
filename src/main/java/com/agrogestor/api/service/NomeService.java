package com.agrogestor.api.service;

import com.agrogestor.api.model.Nome;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.repository.NomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NomeService {

    private final NomeRepository repo;

    public NomeService(NomeRepository repo) {
        this.repo = repo;
    }

    public Nome salvar(Usuario usuario, String nome) {
        return repo.save(new Nome(usuario, nome.trim()));
    }

    public List<Nome> listar(Usuario usuario) {
        return repo.findByUsuarioIdOrderByCreatedAtDesc(usuario.getId());
    }

    public void deletar(Usuario usuario, Long id) {
        Nome nome = repo.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new RuntimeException("Nome não encontrado"));
        repo.delete(nome);
    }
}