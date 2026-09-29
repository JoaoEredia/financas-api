package com.financeiro.financas_api.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.financeiro.financas_api.dto.EvolucaoDiariaDTO;
import com.financeiro.financas_api.dto.GastoPorCategoriaDTO;
import com.financeiro.financas_api.model.TipoTransacao;
import com.financeiro.financas_api.repository.TransacaoRepository;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final TransacaoRepository transacaoRepository;

    public DashboardController(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    @GetMapping("/resumo-mes")
    public ResponseEntity<Map<String, Object>> obterResumoMes(
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Integer mes) {

        LocalDate hoje = LocalDate.now();
        int anoRef = (ano != null) ? ano : hoje.getYear();
        int mesRef = (mes != null) ? mes : hoje.getMonthValue();

        YearMonth mesAtual = YearMonth.of(anoRef, mesRef);
        LocalDate inicioAtual = mesAtual.atDay(1);
        LocalDate fimAtual = mesAtual.atEndOfMonth();

        YearMonth mesAnterior = mesAtual.minusMonths(1);
        LocalDate inicioAnterior = mesAnterior.atDay(1);
        LocalDate fimAnterior = mesAnterior.atEndOfMonth();

        BigDecimal receitasAtual = transacaoRepository.somarTotalPorTipoEPeriodo(TipoTransacao.RECEITA, inicioAtual, fimAtual);
        BigDecimal despesasAtual = transacaoRepository.somarTotalPorTipoEPeriodo(TipoTransacao.DESPESA, inicioAtual, fimAtual);
        BigDecimal despesasAnterior = transacaoRepository.somarTotalPorTipoEPeriodo(TipoTransacao.DESPESA, inicioAnterior, fimAnterior);

        BigDecimal saldoAtual = receitasAtual.subtract(despesasAtual);
        BigDecimal diferencaDespesas = despesasAtual.subtract(despesasAnterior);

        List<GastoPorCategoriaDTO> categorias = transacaoRepository.somarGastosPorCategoria(inicioAtual, fimAtual);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("ano", anoRef);
        resposta.put("mes", mesRef);
        resposta.put("receitas", receitasAtual);
        resposta.put("despesas", despesasAtual);
        resposta.put("saldo", saldoAtual);
        resposta.put("despesasMesAnterior", despesasAnterior);
        resposta.put("diferencaDespesas", diferencaDespesas);
        resposta.put("gastosPorCategoria", categorias);

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/evolucao-diaria")
    public ResponseEntity<List<EvolucaoDiariaDTO>> obterEvolucaoDiaria(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        return ResponseEntity.ok(transacaoRepository.evolucaoGastosPorDia(inicio, fim));
    }
}