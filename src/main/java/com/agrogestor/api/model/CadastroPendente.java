package com.agrogestor.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "cadastros_pendentes")
public class CadastroPendente {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String nome;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String senhaHash;


    @Column(nullable = false, unique = true)
    private String tokenConfirmacao;


    @Column(nullable = false)
    private LocalDateTime tokenExpiraEm;


    @Column(nullable = false)
    private LocalDateTime criadoEm;



    public CadastroPendente(){

    }



    public CadastroPendente(
            String nome,
            String email,
            String senhaHash,
            String tokenConfirmacao
    ){

        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.tokenConfirmacao = tokenConfirmacao;
        this.tokenExpiraEm = LocalDateTime.now().plusHours(24);
        this.criadoEm = LocalDateTime.now();

    }



    public Long getId(){
        return id;
    }


    public String getNome(){
        return nome;
    }


    public String getEmail(){
        return email;
    }


    public String getSenhaHash(){
        return senhaHash;
    }


    public String getTokenConfirmacao(){
        return tokenConfirmacao;
    }


    // Compatibilidade com UsuarioService
    public String getToken(){
        return tokenConfirmacao;
    }


    public LocalDateTime getTokenExpiraEm(){
        return tokenExpiraEm;
    }


    public LocalDateTime getCriadoEm(){
        return criadoEm;
    }

}