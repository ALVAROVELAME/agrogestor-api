package com.agrogestor.api.service.auth;

import com.agrogestor.api.dto.auth.LoginDTO;
import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.repository.conta.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
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

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenhaHash())) {
            throw new BadCredentialsException("E-mail ou senha inválidos.");
        }
        if (!Boolean.TRUE.equals(usuario.getAtivo())) {
            throw new DisabledException("Conta ainda não foi confirmada. Verifique seu e-mail.");
        }

        return usuario;
    }
}