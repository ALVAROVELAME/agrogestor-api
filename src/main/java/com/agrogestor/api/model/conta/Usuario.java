package com.agrogestor.api.model.conta;

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

    @Column(unique = true, length = 64)
    private String recuperacaoTokenHash;
    private LocalDateTime recuperacaoExpiraEm;
    private LocalDateTime recuperacaoSolicitadaEm;
    private Long versaoSessao;

    public String getRecuperacaoTokenHash() { return recuperacaoTokenHash; }
    public LocalDateTime getRecuperacaoExpiraEm() { return recuperacaoExpiraEm; }
    public LocalDateTime getRecuperacaoSolicitadaEm() { return recuperacaoSolicitadaEm; }
    public long getVersaoSessao() { return versaoSessao == null ? 0L : versaoSessao; }
    public void iniciarRecuperacao(String hash, LocalDateTime agora) {
        recuperacaoTokenHash = hash;
        recuperacaoSolicitadaEm = agora;
        recuperacaoExpiraEm = agora.plusMinutes(30);
    }
    public void redefinirSenha(String hash) {
        senhaHash = hash;
        recuperacaoTokenHash = null;
        recuperacaoExpiraEm = null;
        versaoSessao = getVersaoSessao() + 1;
    }


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
