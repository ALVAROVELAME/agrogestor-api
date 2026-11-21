package com.agrogestor.api.model;

import com.agrogestor.api.model.conta.Usuario;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "producoes", uniqueConstraints = @UniqueConstraint(
        name = "uk_producao_animal_data_turno", columnNames = {"animal_id", "data", "turno"}))
public class Producao {
    public enum Turno { MANHA, TARDE, NOITE }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Animal animal;
    @Column(nullable = false)
    private LocalDate data;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10)
    private Turno turno;
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal litros;

    protected Producao() {}
    public Producao(Usuario usuario, Animal animal, LocalDate data, Turno turno, BigDecimal litros) {
        this.usuario = usuario; atualizar(animal, data, turno, litros);
    }
    public void atualizar(Animal animal, LocalDate data, Turno turno, BigDecimal litros) {
        this.animal = animal; this.data = data; this.turno = turno; this.litros = litros;
    }
    public Long getId() { return id; }
    public Animal getAnimal() { return animal; }
    public LocalDate getData() { return data; }
    public Turno getTurno() { return turno; }
    public BigDecimal getLitros() { return litros; }
}
