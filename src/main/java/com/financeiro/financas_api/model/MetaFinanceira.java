package com.financeiro.financas_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "metas_financeiras")
public class MetaFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da meta é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "O valor objetivo é obrigatório")
    @Positive(message = "O valor objetivo deve ser maior que zero")
    @Column(name = "valor_objetivo", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorObjetivo;

    @NotNull(message = "O valor acumulado é obrigatório")
    @PositiveOrZero(message = "O valor acumulado não pode ser negativo")
    @Column(name = "valor_acumulado", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorAcumulado = BigDecimal.ZERO;

    @Column(name = "data_limite")
    private LocalDate dataLimite;

    @Column(length = 30)
    private String corHex; // Opcional, ex: "#4CAF50" para customização visual

    public MetaFinanceira() {}

    public MetaFinanceira(String nome, BigDecimal valorObjetivo, BigDecimal valorAcumulado, LocalDate dataLimite, String corHex) {
        this.nome = nome;
        this.valorObjetivo = valorObjetivo;
        this.valorAcumulado = valorAcumulado != null ? valorAcumulado : BigDecimal.ZERO;
        this.dataLimite = dataLimite;
        this.corHex = corHex;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getValorObjetivo() { return valorObjetivo; }
    public void setValorObjetivo(BigDecimal valorObjetivo) { this.valorObjetivo = valorObjetivo; }

    public BigDecimal getValorAcumulado() { return valorAcumulado; }
    public void setValorAcumulado(BigDecimal valorAcumulado) { this.valorAcumulado = valorAcumulado; }

    public LocalDate getDataLimite() { return dataLimite; }
    public void setDataLimite(LocalDate dataLimite) { this.dataLimite = dataLimite; }

    public String getCorHex() { return corHex; }
    public void setCorHex(String corHex) { this.corHex = corHex; }
}