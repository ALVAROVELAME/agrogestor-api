package com.agrogestor.api.dto;

import com.agrogestor.api.model.Usuario;

public class UsuarioRespostaDTO {

    private Long id;
    private String nome;
    private String email;
    private Boolean ativo;

    public UsuarioRespostaDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.ativo = usuario.getAtivo();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public Boolean getAtivo() { return ativo; }
}