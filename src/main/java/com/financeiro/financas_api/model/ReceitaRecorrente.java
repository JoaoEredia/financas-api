package com.financeiro.financas_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_receitas_recorrentes")
public class ReceitaRecorrente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String descricao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoRegraRecorrencia tipoRegra;

    @Column(name = "dia_do_mes")
    private Integer diaDoMes; // Ex: 20 (utilizado quando tipoRegra == DIA_FIXO)

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "ultimo_mes_lancado", length = 7)
    private String ultimoMesLancado;
    
    @Column(name = "intervalo_dias")
    private Integer intervaloDias; // Ex: 10, 15... // Ex: "2026-10" para evitar duplicações no mesmo mês

    @Column(name = "ultima_data_lancada")
    private LocalDate ultimaDataLancada;

    public Integer getIntervaloDias() { return intervaloDias; }
    public void setIntervaloDias(Integer intervaloDias) { this.intervaloDias = intervaloDias; }

    public LocalDate getUltimaDataLancada() { return ultimaDataLancada; }
    public void setUltimaDataLancada(LocalDate ultimaDataLancada) { this.ultimaDataLancada = ultimaDataLancada; }

    public ReceitaRecorrente() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public TipoRegraRecorrencia getTipoRegra() { return tipoRegra; }
    public void setTipoRegra(TipoRegraRecorrencia tipoRegra) { this.tipoRegra = tipoRegra; }

    public Integer getDiaDoMes() { return diaDoMes; }
    public void setDiaDoMes(Integer diaDoMes) { this.diaDoMes = diaDoMes; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getUltimoMesLancado() { return ultimoMesLancado; }
    public void setUltimoMesLancado(String ultimoMesLancado) { this.ultimoMesLancado = ultimoMesLancado; }
}