package com.agrogestor.api.service;

import com.agrogestor.api.dto.LoginDTO;
import com.agrogestor.api.repository.UsuarioRepository;
import com.agrogestor.api.model.Usuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;



    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ){

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;

    }



    public boolean autenticar(LoginDTO dto){


        Optional<Usuario> usuarioOptional =
                usuarioRepository.findByEmail(dto.getEmail());


        if(usuarioOptional.isEmpty()){

            return false;

        }


        Usuario usuario = usuarioOptional.get();


        if(!usuario.getAtivo()){

            return false;

        }


        return passwordEncoder.matches(
                dto.getSenha(),
                usuario.getSenhaHash()
        );

    }

}