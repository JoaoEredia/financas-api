package com.financeiro.financas_api.service;

import com.financeiro.financas_api.dto.OrcamentoResumoDTO;
import com.financeiro.financas_api.model.Orcamento;
import com.financeiro.financas_api.repository.OrcamentoRepository;
import com.financeiro.financas_api.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrcamentoService {

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<OrcamentoResumoDTO> obterResumoMensal(Integer mes, Integer ano) {
        List<Orcamento> orcamentos = orcamentoRepository.findByMesAndAno(mes, ano);

        return orcamentos.stream().map(orc -> {
            BigDecimal totalGasto = transacaoRepository.somarGastosPorCategoriaEMes(
                orc.getCategoria().getId(), mes, ano
            );
            return new OrcamentoResumoDTO(
                orc.getCategoria().getId(),
                orc.getCategoria().getNome(),
                orc.getValorLimite(),
                totalGasto
            );
        }).toList();
    }
}