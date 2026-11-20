package com.agrogestor.api.service;

import com.agrogestor.api.dto.ProducaoDTO;
import com.agrogestor.api.exception.NotFoundException;
import com.agrogestor.api.model.Animal;
import com.agrogestor.api.model.Producao;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.repository.AnimalRepository;
import com.agrogestor.api.repository.ProducaoRepository;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProducaoService {
    private final ProducaoRepository producoes;
    private final AnimalRepository animais;
    public ProducaoService(ProducaoRepository producoes, AnimalRepository animais) {
        this.producoes = producoes; this.animais = animais;
    }
    @Transactional(readOnly = true)
    public Page<ProducaoDTO.Saida> listar(Usuario usuario, LocalDate inicio, LocalDate fim, Long animalId, Producao.Turno turno, int pagina, int tamanho) {
        if (pagina < 0 || tamanho < 1 || tamanho > 100) throw new IllegalArgumentException("Paginação inválida");
        if (inicio != null && fim != null && inicio.isAfter(fim)) throw new IllegalArgumentException("Data inicial posterior à final");
        return producoes.buscar(usuario.getId(), inicio, fim, animalId, turno,
                PageRequest.of(pagina, tamanho, Sort.by(Sort.Order.desc("data"), Sort.Order.desc("id")))).map(ProducaoDTO.Saida::new);
    }
    @Transactional
    public ProducaoDTO.Saida criar(Usuario usuario, ProducaoDTO.Entrada dados) {
        Animal animal = animalDoUsuario(usuario, dados.animalId());
        return new ProducaoDTO.Saida(producoes.saveAndFlush(new Producao(usuario, animal, dados.data(), dados.turno(), dados.litros())));
    }
    @Transactional
    public ProducaoDTO.Saida atualizar(Usuario usuario, Long id, ProducaoDTO.Entrada dados) {
        Producao producao = obter(usuario, id);
        producao.atualizar(animalDoUsuario(usuario, dados.animalId()), dados.data(), dados.turno(), dados.litros());
        return new ProducaoDTO.Saida(producoes.saveAndFlush(producao));
    }
    @Transactional
    public void excluir(Usuario usuario, Long id) { producoes.delete(obter(usuario, id)); }
    private Animal animalDoUsuario(Usuario usuario, Long id) {
        return animais.findByIdAndUsuarioId(id, usuario.getId()).orElseThrow(() -> new NotFoundException("Animal não encontrado"));
    }
    private Producao obter(Usuario usuario, Long id) {
        return producoes.findByIdAndUsuarioId(id, usuario.getId()).orElseThrow(() -> new NotFoundException("Produção não encontrada"));
    }
}
