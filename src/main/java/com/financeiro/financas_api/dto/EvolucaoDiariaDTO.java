package com.financeiro.financas_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EvolucaoDiariaDTO(LocalDate data, BigDecimal total) {}