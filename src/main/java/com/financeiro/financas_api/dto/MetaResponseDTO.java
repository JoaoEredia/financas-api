package com.financeiro.financas_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MetaResponseDTO(
    Long id,
    String nome,
    BigDecimal valorObjetivo,
    BigDecimal valorAcumulado,
    BigDecimal valorRestante,
    BigDecimal percentualConcluido,
    LocalDate dataLimite,
    String corHex,
    boolean concluida
) {}