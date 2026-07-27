package com.agrogestor.api.service;


import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;



@Service
public class EmailConfirmationService {


    private final UsuarioRepository usuarioRepository;



    public EmailConfirmationService(
            UsuarioRepository usuarioRepository
    ){

        this.usuarioRepository = usuarioRepository;

    }



    public boolean confirmarEmail(String token){


        Optional<Usuario> usuarioOptional =
                usuarioRepository.findByTokenConfirmacao(token);



        if(usuarioOptional.isEmpty()){

            return false;

        }



        Usuario usuario = usuarioOptional.get();



        if(
                usuario.getTokenExpiraEm() == null ||
                        usuario.getTokenExpiraEm()
                                .isBefore(LocalDateTime.now())
        ){

            return false;

        }



        usuario.ativarConta();


        usuarioRepository.save(usuario);



        return true;

    }


}