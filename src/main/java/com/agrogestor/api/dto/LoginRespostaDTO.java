package com.agrogestor.api.dto;


public class LoginRespostaDTO {


    private String mensagem;



    public LoginRespostaDTO(String mensagem){

        this.mensagem = mensagem;

    }



    public String getMensagem(){

        return mensagem;

    }

}