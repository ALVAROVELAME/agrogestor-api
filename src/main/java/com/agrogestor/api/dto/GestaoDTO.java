package com.agrogestor.api.dto;

import com.agrogestor.api.model.RegistroGestao;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class GestaoDTO {
    private GestaoDTO() {}
    public record Tarefa(@NotBlank @Size(max = 160) String titulo, @NotNull LocalDate data, Long animalId) {}
    public record AtualizarTarefa(@Size(max = 160) String titulo, LocalDate data, Boolean concluida) {}
    public record Evento(@NotNull Long animalId, @NotNull LocalDate data,
            @NotNull RegistroGestao.TipoEvento tipo, @NotBlank @Size(max = 160) String descricao) {}
    public record Financeiro(@NotNull LocalDate data, @NotNull RegistroGestao.TipoFinanceiro tipo,
            @NotBlank @Size(max = 160) String descricao,
            @NotNull @DecimalMin("0.01") @Digits(integer = 12, fraction = 2) BigDecimal valor) {}
    public record Saida(Long id, LocalDate data, String descricao, String subtipo, BigDecimal valor,
            Boolean concluida, Long animalId, String animalNome) {
        public Saida(RegistroGestao r) { this(r.getId(), r.getData(), r.getDescricao(), r.getSubtipo(), r.getValor(),
                r.getConcluida(), r.getAnimal() == null ? null : r.getAnimal().getId(),
                r.getAnimal() == null ? null : r.getAnimal().getNome()); }
    }
}
