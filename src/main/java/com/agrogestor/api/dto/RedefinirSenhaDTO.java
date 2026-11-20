package com.agrogestor.api.dto;

import com.agrogestor.api.validation.BcryptPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RedefinirSenhaDTO(
        @NotBlank @Size(max = 128) String token,
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
        @BcryptPassword String senha) {
    @Override public String toString() { return "RedefinirSenhaDTO[credenciais protegidas]"; }
}
