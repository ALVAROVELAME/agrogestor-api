package com.agrogestor.api.service;

import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;



    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ){

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;

    }




    public Usuario criarUsuario(
            String nome,
            String email,
            String senha
    ){

        if(usuarioRepository.existsByEmail(email)){
            throw new RuntimeException("Email já cadastrado");
        }


        String senhaHash = passwordEncoder.encode(senha);


        Usuario usuario = new Usuario(
                nome,
                email,
                senhaHash
        );


        return usuarioRepository.save(usuario);

    }


}