package com.agrogestor.api.service;

import com.agrogestor.api.dto.GestaoDTO;
import com.agrogestor.api.exception.NotFoundException;
import com.agrogestor.api.model.*;
import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.repository.*;
import java.time.LocalDate;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestaoService {
    private final RegistroGestaoRepository registros;
    private final AnimalRepository animais;
    public GestaoService(RegistroGestaoRepository registros, AnimalRepository animais) { this.registros = registros; this.animais = animais; }

    @Transactional(readOnly = true)
    public Page<GestaoDTO.Saida> listar(Usuario usuario, RegistroGestao.Tipo tipo, LocalDate inicio, LocalDate fim, Long animalId, Boolean concluida, String subtipo, int pagina, int tamanho) {
        if (pagina < 0 || tamanho < 1 || tamanho > 100) throw new IllegalArgumentException("Paginação inválida");
        if (inicio != null && fim != null && inicio.isAfter(fim)) throw new IllegalArgumentException("Data inicial posterior à final");
        return registros.buscar(usuario.getId(), tipo, inicio, fim, animalId, concluida, subtipo,
                PageRequest.of(pagina, tamanho, Sort.by(Sort.Order.desc("data"), Sort.Order.desc("id")))).map(GestaoDTO.Saida::new);
    }
    private Animal animal(Usuario usuario, Long id) {
        if (id == null) return null;
        return animais.findByIdAndUsuarioId(id, usuario.getId()).orElseThrow(() -> new NotFoundException("Animal não encontrado"));
    }
    private RegistroGestao obter(Usuario usuario, Long id, RegistroGestao.Tipo tipo) {
        return registros.findByIdAndUsuarioIdAndTipo(id, usuario.getId(), tipo).orElseThrow(() -> new NotFoundException("Registro não encontrado"));
    }
    @Transactional
    public GestaoDTO.Saida criarTarefa(Usuario usuario, GestaoDTO.Tarefa dados) {
        RegistroGestao r = new RegistroGestao(usuario, RegistroGestao.Tipo.TAREFA);
        r.setDescricao(dados.titulo().trim()); r.setData(dados.data()); r.setAnimal(animal(usuario, dados.animalId())); r.setConcluida(false);
        return new GestaoDTO.Saida(registros.save(r));
    }
    @Transactional
    public GestaoDTO.Saida atualizarTarefa(Usuario usuario, Long id, GestaoDTO.AtualizarTarefa dados) {
        RegistroGestao r = obter(usuario, id, RegistroGestao.Tipo.TAREFA);
        if (dados.titulo() != null) {
            if (dados.titulo().isBlank()) throw new IllegalArgumentException("Título é obrigatório");
            r.setDescricao(dados.titulo().trim());
        }
        if (dados.data() != null) r.setData(dados.data());
        if (dados.concluida() != null) r.setConcluida(dados.concluida());
        return new GestaoDTO.Saida(registros.save(r));
    }
    @Transactional
    public GestaoDTO.Saida salvarEvento(Usuario usuario, Long id, GestaoDTO.Evento dados) {
        RegistroGestao r = id == null ? new RegistroGestao(usuario, RegistroGestao.Tipo.EVENTO) : obter(usuario, id, RegistroGestao.Tipo.EVENTO);
        r.setAnimal(animal(usuario, dados.animalId())); r.setData(dados.data()); r.setSubtipo(dados.tipo().name()); r.setDescricao(dados.descricao().trim());
        return new GestaoDTO.Saida(registros.save(r));
    }
    @Transactional
    public GestaoDTO.Saida salvarFinanceiro(Usuario usuario, Long id, GestaoDTO.Financeiro dados) {
        RegistroGestao r = id == null ? new RegistroGestao(usuario, RegistroGestao.Tipo.FINANCEIRO) : obter(usuario, id, RegistroGestao.Tipo.FINANCEIRO);
        r.setData(dados.data()); r.setSubtipo(dados.tipo().name()); r.setDescricao(dados.descricao().trim()); r.setValor(dados.valor());
        return new GestaoDTO.Saida(registros.save(r));
    }
    @Transactional
    public void excluir(Usuario usuario, Long id, RegistroGestao.Tipo tipo) { registros.delete(obter(usuario, id, tipo)); }
}
