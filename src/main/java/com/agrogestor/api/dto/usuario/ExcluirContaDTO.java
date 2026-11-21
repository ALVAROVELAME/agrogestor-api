package com.agrogestor.api.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public class ExcluirContaDTO {

    @NotBlank(message = "Senha é obrigatória para confirmar a exclusão")
    @com.agrogestor.api.validation.BcryptPassword
    private String senha;

    public ExcluirContaDTO() {}

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}