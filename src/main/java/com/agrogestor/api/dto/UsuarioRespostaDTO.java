package com.agrogestor.api.dto;


import com.agrogestor.api.model.Usuario;



public class UsuarioRespostaDTO {


    private Long id;

    private String nome;

    private String email;



    public UsuarioRespostaDTO(Usuario usuario){

        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();

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

}