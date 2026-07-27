package com.agrogestor.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "usuarios")
public class Usuario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String nome;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String senhaHash;


    @Column(nullable = false)
    private Boolean ativo = false;


    @Column(unique = true)
    private String tokenConfirmacao;


    private LocalDateTime tokenExpiraEm;


    private LocalDateTime emailConfirmadoEm;


    @Column(nullable = false)
    private LocalDateTime criadoEm;


    public Usuario() {

    }


    public Usuario(
            String nome,
            String email,
            String senhaHash
    ){

        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.ativo = false;
        this.criadoEm = LocalDateTime.now();

    }


    public Long getId() {
        return id;
    }


    public String getNome() {
        return nome;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getEmail() {
        return email;
    }


    public String getSenhaHash() {
        return senhaHash;
    }


    public Boolean getAtivo() {
        return ativo;
    }


    public void ativarConta(){

        this.ativo = true;
        this.emailConfirmadoEm = LocalDateTime.now();
        this.tokenConfirmacao = null;
        this.tokenExpiraEm = null;

    }


    public String getTokenConfirmacao() {
        return tokenConfirmacao;
    }


    public void setTokenConfirmacao(String tokenConfirmacao) {
        this.tokenConfirmacao = tokenConfirmacao;
    }


    public LocalDateTime getTokenExpiraEm() {
        return tokenExpiraEm;
    }


    public void setTokenExpiraEm(LocalDateTime tokenExpiraEm) {
        this.tokenExpiraEm = tokenExpiraEm;
    }


    public LocalDateTime getEmailConfirmadoEm() {
        return emailConfirmadoEm;
    }


    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

}