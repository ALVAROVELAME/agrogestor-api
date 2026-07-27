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
    private Boolean ativo = true;


    @Column(nullable = false)
    private LocalDateTime criadoEm;



    public Usuario() {

    }



    public Usuario(String nome, String email, String senhaHash) {

        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.criadoEm = LocalDateTime.now();

    }



    public Long getId() {
        return id;
    }


    public String getNome() {
        return nome;
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


    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

}