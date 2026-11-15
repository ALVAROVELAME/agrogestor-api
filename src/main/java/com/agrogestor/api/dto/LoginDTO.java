package com.agrogestor.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginDTO {

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @jakarta.validation.constraints.Size(max = 255, message = "E-mail muito longo")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @com.agrogestor.api.validation.BcryptPassword
    private String senha;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}