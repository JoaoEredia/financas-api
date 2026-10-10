package com.financeiro.financas_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MetaRequestDTO(
    @NotBlank(message = "O nome não pode ser vazio")
    String nome,

    @NotNull(message = "O valor objetivo é obrigatório")
    @Positive(message = "O valor objetivo deve ser maior que zero")
    BigDecimal valorObjetivo,

    @PositiveOrZero(message = "O valor acumulado não pode ser negativo")
    BigDecimal valorAcumulado,

    LocalDate dataLimite,
    String corHex
) {}