package com.agrogestor.api.service;

import com.agrogestor.api.model.Nome;
import com.agrogestor.api.repository.NomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NomeService {

    private final NomeRepository nomeRepository;

    public NomeService(NomeRepository nomeRepository) {
        this.nomeRepository = nomeRepository;
    }


    public Nome salvar(Nome nome) {
        return nomeRepository.save(nome);
    }


    public List<Nome> listar() {
        return nomeRepository.findAll();
    }


    public void deletar(Long id) {
        nomeRepository.deleteById(id);
    }

}