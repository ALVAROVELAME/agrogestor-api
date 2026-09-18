package com.agrogestor.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "animais")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Dono do animal. Todo animal pertence a UM usuário.
     * O usuário logado nunca vê animais de outros usuários.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String brinco;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private Double producaoDiaria;

    @Column(nullable = false)
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

    public Animal() {}

    public Animal(
            Usuario usuario,
            String brinco,
            String nome,
            Categoria categoria,
            Double producaoDiaria
    ) {
        this.usuario = usuario;
        this.brinco = brinco;
        this.nome = nome;
        this.categoria = categoria;
        this.producaoDiaria = producaoDiaria;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    // Getters / setters

    public Long getId() { return id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getBrinco() { return brinco; }
    public void setBrinco(String brinco) { this.brinco = brinco; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Double getProducaoDiaria() { return producaoDiaria; }
    public void setProducaoDiaria(Double producaoDiaria) { this.producaoDiaria = producaoDiaria; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}