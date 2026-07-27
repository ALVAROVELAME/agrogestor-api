package com.agrogestor.api.service;


import com.agrogestor.api.dto.UsuarioCadastroDTO;
import com.agrogestor.api.model.CadastroPendente;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.repository.CadastroPendenteRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;



@Service
public class UsuarioService {


    private final CadastroPendenteRepository cadastroPendenteRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;



    public UsuarioService(
            CadastroPendenteRepository cadastroPendenteRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {

        this.cadastroPendenteRepository = cadastroPendenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;

    }



    public CadastroPendente cadastrar(UsuarioCadastroDTO dto) {


        String senhaHash =
                passwordEncoder.encode(dto.getSenha());



        String token =
                UUID.randomUUID().toString();



        CadastroPendente cadastro =
                new CadastroPendente(
                        dto.getNome(),
                        dto.getEmail(),
                        senhaHash,
                        token
                );



        CadastroPendente salvo =
                cadastroPendenteRepository.save(cadastro);



        emailService.enviarEmailConfirmacao(
                salvo.getEmail(),
                salvo.getNome(),
                salvo.getToken()
        );



        return salvo;

    }


}