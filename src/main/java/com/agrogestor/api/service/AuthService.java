package com.agrogestor.api.service;

import com.agrogestor.api.dto.LoginDTO;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.repository.CadastroPendenteRepository;
import com.agrogestor.api.repository.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    /**
     * Autentica o usuário e retorna a entidade Usuario.
     * Lança exceção em caso de falha.
     */
    public Usuario autenticar(LoginDTO dto) {

        Usuario usuario = usuarioRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new BadCredentialsException("E-mail ou senha inválidos.")
                );

        if (!usuario.getAtivo()) {
            throw new DisabledException(
                    "Conta ainda não foi confirmada. Verifique seu e-mail."
            );
        }

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenhaHash())) {
            throw new BadCredentialsException("E-mail ou senha inválidos.");
        }

        return usuario;
    }
}