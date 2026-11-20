package com.agrogestor.api.dto;

import com.agrogestor.api.model.Producao;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class ProducaoDTO {
    private ProducaoDTO() {}
    public record Entrada(
            @NotNull Long animalId,
            @NotNull @PastOrPresent LocalDate data,
            @NotNull Producao.Turno turno,
            @NotNull @DecimalMin("0.01") @DecimalMax("200.00") @Digits(integer = 3, fraction = 2) BigDecimal litros) {}
    public record Saida(Long id, Long animalId, String animalNome, String brinco, LocalDate data, Producao.Turno turno, BigDecimal litros) {
        public Saida(Producao p) { this(p.getId(), p.getAnimal().getId(), p.getAnimal().getNome(), p.getAnimal().getBrinco(), p.getData(), p.getTurno(), p.getLitros()); }
    }
}
