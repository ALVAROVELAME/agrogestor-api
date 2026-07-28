package com.agrogestor.api.service;


import com.agrogestor.api.dto.UsuarioCadastroDTO;
import com.agrogestor.api.model.CadastroPendente;
import com.agrogestor.api.repository.CadastroPendenteRepository;
import com.agrogestor.api.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;



@Service
public class UsuarioService {


    private final CadastroPendenteRepository cadastroPendenteRepository;

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;



    public UsuarioService(
            CadastroPendenteRepository cadastroPendenteRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService
    ) {

        this.cadastroPendenteRepository = cadastroPendenteRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;

    }



    public CadastroPendente cadastrar(UsuarioCadastroDTO dto) {


        if(usuarioRepository.findByEmail(dto.getEmail()).isPresent()){

            throw new RuntimeException(
                    "Email já cadastrado"
            );

        }



        if(cadastroPendenteRepository.findByEmail(dto.getEmail()).isPresent()){

            throw new RuntimeException(
                    "Email aguardando confirmação"
            );

        }



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