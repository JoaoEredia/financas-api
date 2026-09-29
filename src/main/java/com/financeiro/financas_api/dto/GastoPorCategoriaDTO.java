package com.financeiro.financas_api.dto;

import java.math.BigDecimal;

public record GastoPorCategoriaDTO(String categoria, BigDecimal total) {}