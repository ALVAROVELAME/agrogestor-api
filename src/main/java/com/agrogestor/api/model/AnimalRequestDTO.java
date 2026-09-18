package com.agrogestor.api.dto;

import jakarta.validation.constraints.*;

public class AnimalRequestDTO {

    @NotBlank(message = "Brinco é obrigatório")
    @Size(max = 20, message = "Brinco muito longo")
    private String brinco;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 40, message = "Nome muito longo")
    private String nome;

    @NotNull(message = "Categoria é obrigatória")
    private String categoria;

    @NotNull(message = "Produção diária é obrigatória")
    @DecimalMin(value = "0.0", message = "Produção não pode ser negativa")
    @DecimalMax(value = "200.0", message = "Produção irreal")
    private Double producaoDiaria;

    public String getBrinco() { return brinco; }
    public void setBrinco(String brinco) { this.brinco = brinco; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Double getProducaoDiaria() { return producaoDiaria; }
    public void setProducaoDiaria(Double producaoDiaria) { this.producaoDiaria = producaoDiaria; }
}