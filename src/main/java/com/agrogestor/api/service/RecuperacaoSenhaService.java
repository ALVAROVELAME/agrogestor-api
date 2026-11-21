package com.agrogestor.api.service;

import com.agrogestor.api.repository.UsuarioRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecuperacaoSenhaService {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final EmailService email;

    public RecuperacaoSenhaService(UsuarioRepository usuarios, PasswordEncoder encoder, EmailService email) {
        this.usuarios = usuarios;
        this.encoder = encoder;
        this.email = email;
    }

    @Transactional
    public void solicitar(String endereco) {
        usuarios.buscarPorEmailParaRecuperacao(endereco.trim()).filter(u -> Boolean.TRUE.equals(u.getAtivo())).ifPresent(u -> {
            LocalDateTime agora = LocalDateTime.now();
            if (u.getRecuperacaoSolicitadaEm() != null && u.getRecuperacaoSolicitadaEm().plusMinutes(1).isAfter(agora)) return;
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            u.iniciarRecuperacao(hash(token), agora);
            email.enviarEmailRecuperacao(u.getEmail(), token);
        });
    }

    @Transactional
    public boolean redefinir(String token, String senha) {
        return usuarios.buscarPorTokenDeRecuperacao(hash(token)).filter(u ->
                Boolean.TRUE.equals(u.getAtivo()) && u.getRecuperacaoExpiraEm() != null
                && u.getRecuperacaoExpiraEm().isAfter(LocalDateTime.now())).map(u -> {
            u.redefinirSenha(encoder.encode(senha));
            return true;
        }).orElse(false);
    }

    static String hash(String token) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
