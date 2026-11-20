package com.agrogestor.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/** Registro persistido pertencente a um único usuário; o vínculo com animal é opcional. */
@Entity
@Table(name = "registros_gestao", indexes = @Index(name = "idx_gestao_usuario_tipo_data", columnList = "usuario_id,tipo,data"))
public class RegistroGestao {
    public enum Tipo { TAREFA, EVENTO, FINANCEIRO }
    public enum TipoEvento { CONSULTA, VACINACAO, TRATAMENTO, INSEMINACAO, PARTO, OUTRO }
    public enum TipoFinanceiro { RECEITA, DESPESA }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id")
    @OnDelete(action = OnDeleteAction.CASCADE) private Animal animal;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Tipo tipo;
    @Column(nullable = false) private LocalDate data;
    @Column(nullable = false, length = 160) private String descricao;
    @Column(length = 30) private String subtipo;
    @Column(precision = 14, scale = 2) private BigDecimal valor;
    private Boolean concluida;

    protected RegistroGestao() {}
    public RegistroGestao(Usuario usuario, Tipo tipo) { this.usuario = usuario; this.tipo = tipo; }
    public Long getId() { return id; }
    public Animal getAnimal() { return animal; }
    public void setAnimal(Animal animal) { this.animal = animal; }
    public Tipo getTipo() { return tipo; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getSubtipo() { return subtipo; }
    public void setSubtipo(String subtipo) { this.subtipo = subtipo; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public Boolean getConcluida() { return concluida; }
    public void setConcluida(Boolean concluida) { this.concluida = concluida; }
}
