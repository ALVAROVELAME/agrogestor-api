package com.agrogestor.api.dto;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** Campos opcionais; texto vazio remove o valor existente. */
public record FichaAnimalDTO(@PastOrPresent LocalDate dataNascimento,
        @Size(max = 100) String raca, @Size(max = 100) String origem,
        @Size(max = 1000) String observacoes) {}
