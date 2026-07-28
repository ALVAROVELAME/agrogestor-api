package com.agrogestor.api.service;

import com.agrogestor.api.dto.LoginDTO;
import com.agrogestor.api.model.CadastroPendente;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.repository.CadastroPendenteRepository;
import com.agrogestor.api.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    private final CadastroPendenteRepository cadastroPendenteRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            CadastroPendenteRepository cadastroPendenteRepository,
            PasswordEncoder passwordEncoder
    ) {

        this.usuarioRepository = usuarioRepository;
        this.cadastroPendenteRepository = cadastroPendenteRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public String autenticar(LoginDTO dto) {

        Optional<Usuario> usuarioOptional =
                usuarioRepository.findByEmail(dto.getEmail());

        if (usuarioOptional.isPresent()) {

            Usuario usuario = usuarioOptional.get();

            if (!usuario.getAtivo()) {

                return "Conta ainda não foi confirmada.";

            }

            if (passwordEncoder.matches(
                    dto.getSenha(),
                    usuario.getSenhaHash()
            )) {

                return "Login realizado com sucesso";

            }

            return "Email ou senha inválidos.";

        }

        Optional<CadastroPendente> cadastroOptional =
                cadastroPendenteRepository.findByEmail(dto.getEmail());

        if (cadastroOptional.isPresent()) {

            return "Conta ainda não foi confirmada. Verifique seu e-mail.";

        }

        return "Email ou senha inválidos.";

    }

}