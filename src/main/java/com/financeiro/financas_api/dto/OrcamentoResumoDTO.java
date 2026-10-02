package com.financeiro.financas_api.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrcamentoResumoDTO {
    private Long categoriaId;
    private String categoriaNome;
    private BigDecimal limite;
    private BigDecimal gasto;
    private BigDecimal saldoRestante;
    private Double percentualUtilizado;
    private String status; // NORMAL, ATENCAO, ULTRAPASSADO

    public OrcamentoResumoDTO(Long categoriaId, String categoriaNome, BigDecimal limite, BigDecimal gasto) {
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
        this.limite = limite != null ? limite : BigDecimal.ZERO;
        this.gasto = gasto != null ? gasto : BigDecimal.ZERO;

        // Saldo Restante = Limite - Gasto
        this.saldoRestante = this.limite.subtract(this.gasto);

        // Percentual Utilizado = (Gasto / Limite) * 100
        if (this.limite.compareTo(BigDecimal.ZERO) > 0) {
            this.percentualUtilizado = this.gasto
                    .divide(this.limite, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .doubleValue();
        } else {
            this.percentualUtilizado = 0.0;
        }

        // Definir status
        if (this.percentualUtilizado > 100.0) {
            this.status = "ULTRAPASSADO";
        } else if (this.percentualUtilizado >= 80.0) {
            this.status = "ATENCAO";
        } else {
            this.status = "NORMAL";
        }
    }

    // Getters
    public Long getCategoriaId() { return categoriaId; }
    public String getCategoriaNome() { return categoriaNome; }
    public BigDecimal getLimite() { return limite; }
    public BigDecimal getGasto() { return gasto; }
    public BigDecimal getSaldoRestante() { return saldoRestante; }
    public Double getPercentualUtilizado() { return percentualUtilizado; }
    public String getStatus() { return status; }
}