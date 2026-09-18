package com.agrogestor.api.dto;

public class LoginRespostaDTO {

    private boolean sucesso;
    private String mensagem;
    private String token;
    private UsuarioRespostaDTO usuario;

    public LoginRespostaDTO(boolean sucesso, String mensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }

    public LoginRespostaDTO(
            boolean sucesso,
            String mensagem,
            String token,
            UsuarioRespostaDTO usuario
    ) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.token = token;
        this.usuario = usuario;
    }

    public boolean isSucesso() { return sucesso; }
    public String getMensagem() { return mensagem; }
    public String getToken() { return token; }
    public UsuarioRespostaDTO getUsuario() { return usuario; }
}