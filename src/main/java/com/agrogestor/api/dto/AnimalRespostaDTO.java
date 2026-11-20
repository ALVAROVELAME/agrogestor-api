package com.agrogestor.api.dto;

import com.agrogestor.api.model.Animal;

public class AnimalRespostaDTO {

    private Long id;
    private String brinco;
    private String nome;
    private String categoria;
    private Double producaoDiaria;
    private java.time.LocalDate dataNascimento;
    private String raca;
    private String origem;
    private String observacoes;

    public AnimalRespostaDTO(Animal a) {
        this.id = a.getId();
        this.brinco = a.getBrinco();
        this.nome = a.getNome();
        this.categoria = a.getCategoria().name();
        this.producaoDiaria = a.getProducaoDiaria();
        this.dataNascimento = a.getDataNascimento();
        this.raca = a.getRaca();
        this.origem = a.getOrigem();
        this.observacoes = a.getObservacoes();
    }

    public Long getId() { return id; }
    public String getBrinco() { return brinco; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public Double getProducaoDiaria() { return producaoDiaria; }
    public java.time.LocalDate getDataNascimento() { return dataNascimento; }
    public String getRaca() { return raca; }
    public String getOrigem() { return origem; }
    public String getObservacoes() { return observacoes; }
}
