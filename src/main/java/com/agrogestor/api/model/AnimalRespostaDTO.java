package com.agrogestor.api.dto;

import com.agrogestor.api.model.Animal;

public class AnimalRespostaDTO {

    private Long id;
    private String brinco;
    private String nome;
    private String categoria;
    private Double producaoDiaria;

    public AnimalRespostaDTO(Animal a) {
        this.id = a.getId();
        this.brinco = a.getBrinco();
        this.nome = a.getNome();
        this.categoria = a.getCategoria().name();
        this.producaoDiaria = a.getProducaoDiaria();
    }

    public Long getId() { return id; }
    public String getBrinco() { return brinco; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public Double getProducaoDiaria() { return producaoDiaria; }
}