package com.agrogestor.api.service;

import com.agrogestor.api.dto.usuario.UsuarioCadastroDTO;
import com.agrogestor.api.model.conta.CadastroPendente;
import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.repository.AnimalRepository;
import com.agrogestor.api.repository.conta.CadastroPendenteRepository;
import com.agrogestor.api.repository.conta.UsuarioRepository;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UsuarioService {

    private final CadastroPendenteRepository cadastroPendenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final AnimalRepository animalRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UsuarioService(
            CadastroPendenteRepository cadastroPendenteRepository,
            UsuarioRepository usuarioRepository,
            AnimalRepository animalRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {
        this.cadastroPendenteRepository = cadastroPendenteRepository;
        this.usuarioRepository = usuarioRepository;
        this.animalRepository = animalRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public CadastroPendente cadastrar(UsuarioCadastroDTO dto) {

        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new com.agrogestor.api.exception.ConflictException("Email já cadastrado");
        }

        cadastroPendenteRepository.findByEmail(dto.getEmail()).ifPresent(pendente -> {
            if (pendente.getTokenExpiraEm() != null && pendente.getTokenExpiraEm().isAfter(java.time.LocalDateTime.now())) {
                throw new com.agrogestor.api.exception.ConflictException("Email aguardando confirmação");
            }
            cadastroPendenteRepository.delete(pendente);
            cadastroPendenteRepository.flush();
        });

        String senhaHash = passwordEncoder.encode(dto.getSenha());
        String token = UUID.randomUUID().toString();

        CadastroPendente cadastro = new CadastroPendente(
                dto.getNome(),
                dto.getEmail(),
                senhaHash,
                token
        );

        CadastroPendente salvo = cadastroPendenteRepository.save(cadastro);

        emailService.enviarEmailConfirmacao(
                salvo.getEmail(),
                salvo.getNome(),
                salvo.getTokenConfirmacao()
        );

        return salvo;
    }

    /**
     * Exclui a conta do usuário e todos os dados vinculados (animais).
     * Exige a senha atual para confirmar a operação.
     */
    @Transactional
    public void excluirConta(Usuario usuario, String senha) {

        // 1. Revalida a senha antes da ação destrutiva
        if (!passwordEncoder.matches(senha, usuario.getSenhaHash())) {
            throw new BadCredentialsException("Senha incorreta. Exclusão cancelada.");
        }

        Long usuarioId = usuario.getId();

        // 2. Remove dependências (animais)
        animalRepository.deleteByUsuarioId(usuarioId);

        // 3. Remove cadastros pendentes com o mesmo e-mail (se houver)
        cadastroPendenteRepository.findByEmail(usuario.getEmail())
                .ifPresent(cadastroPendenteRepository::delete);

        // 4. Remove o usuário
        usuarioRepository.delete(usuario);
    }
}