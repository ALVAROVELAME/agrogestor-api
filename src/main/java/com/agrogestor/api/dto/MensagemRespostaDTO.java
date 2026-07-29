package com.agrogestor.api.dto;


public class MensagemRespostaDTO {


    private boolean sucesso;

    private String mensagem;



    public MensagemRespostaDTO(
            boolean sucesso,
            String mensagem
    ){

        this.sucesso = sucesso;
        this.mensagem = mensagem;

    }



    public boolean isSucesso(){

        return sucesso;

    }



    public String getMensagem(){

        return mensagem;

    }

}