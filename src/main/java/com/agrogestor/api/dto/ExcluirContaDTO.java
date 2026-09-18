package com.agrogestor.api.dto;

import jakarta.validation.constraints.NotBlank;

public class ExcluirContaDTO {

    @NotBlank(message = "Senha é obrigatória para confirmar a exclusão")
    private String senha;

    public ExcluirContaDTO() {}

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}