package com.agrogestor.api.service;

import com.agrogestor.api.dto.UsuarioCadastroDTO;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;

    }

    public Usuario cadastrar(UsuarioCadastroDTO dto) {

        String senhaHash = passwordEncoder.encode(dto.getSenha());

        Usuario usuario = new Usuario(
                dto.getNome(),
                dto.getEmail(),
                senhaHash
        );

        String token = UUID.randomUUID().toString();

        usuario.setTokenConfirmacao(token);

        usuario.setTokenExpiraEm(
                LocalDateTime.now().plusHours(24)
        );

        Usuario salvo = usuarioRepository.save(usuario);

        emailService.enviarEmailConfirmacao(
                salvo.getEmail(),
                salvo.getNome(),
                salvo.getTokenConfirmacao()
        );

        return salvo;

    }

}