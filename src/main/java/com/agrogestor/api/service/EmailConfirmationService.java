package com.agrogestor.api.service;

import com.agrogestor.api.model.CadastroPendente;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.repository.CadastroPendenteRepository;
import com.agrogestor.api.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmailConfirmationService {

    private final CadastroPendenteRepository cadastroPendenteRepository;
    private final UsuarioRepository usuarioRepository;

    public EmailConfirmationService(
            CadastroPendenteRepository cadastroPendenteRepository,
            UsuarioRepository usuarioRepository
    ) {

        this.cadastroPendenteRepository = cadastroPendenteRepository;
        this.usuarioRepository = usuarioRepository;

    }

    public boolean confirmarEmail(String token) {

        Optional<CadastroPendente> cadastroOptional =
                cadastroPendenteRepository.findByTokenConfirmacao(token);

        if (cadastroOptional.isEmpty()) {
            return false;
        }

        CadastroPendente cadastro = cadastroOptional.get();

        if (
                cadastro.getTokenExpiraEm() == null ||
                        cadastro.getTokenExpiraEm().isBefore(LocalDateTime.now())
        ) {
            return false;
        }

        Usuario usuario = new Usuario(
                cadastro.getNome(),
                cadastro.getEmail(),
                cadastro.getSenhaHash()
        );

        usuario.ativarConta();

        usuarioRepository.save(usuario);

        cadastroPendenteRepository.delete(cadastro);

        return true;

    }

}