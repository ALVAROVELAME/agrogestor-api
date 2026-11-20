package com.agrogestor.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SolicitarRecuperacaoDTO(
        @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido")
        @Size(max = 255, message = "E-mail muito longo") String email) {}
