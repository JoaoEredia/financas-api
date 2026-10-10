package com.financeiro.financas_api.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AporteRequestDTO(
    @NotNull(message = "O valor do ajuste/aporte é obrigatório")
    BigDecimal valor // Pode ser positivo (juntou mais) ou negativo (resgatou)
) {}