package com.financeiro.financas_api.service;

import com.financeiro.financas_api.dto.AporteRequestDTO;
import com.financeiro.financas_api.dto.MetaRequestDTO;
import com.financeiro.financas_api.dto.MetaResponseDTO;
import com.financeiro.financas_api.model.MetaFinanceira;
import com.financeiro.financas_api.repository.MetaFinanceiraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class MetaFinanceiraService {

    private final MetaFinanceiraRepository repository;
    private final TransacaoService transacaoService; // <-- Certifique-se de declarar aqui

    // O construtor precisa receber os dois:
    public MetaFinanceiraService(MetaFinanceiraRepository repository, TransacaoService transacaoService) {
        this.repository = repository;
        this.transacaoService = transacaoService; // <-- E inicializar aqui
    }

    @Transactional(readOnly = true)
    public List<MetaResponseDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public MetaResponseDTO buscarPorId(Long id) {
        MetaFinanceira meta = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meta não encontrada com ID: " + id));
        return toResponseDTO(meta);
    }

    @Transactional
    public MetaResponseDTO criar(MetaRequestDTO dto) {
        MetaFinanceira meta = new MetaFinanceira(
                dto.nome(),
                dto.valorObjetivo(),
                dto.valorAcumulado() != null ? dto.valorAcumulado() : BigDecimal.ZERO,
                dto.dataLimite(),
                dto.corHex()
        );
        return toResponseDTO(repository.save(meta));
    }

    @Transactional
    public MetaResponseDTO atualizar(Long id, MetaRequestDTO dto) {
        MetaFinanceira meta = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meta não encontrada com ID: " + id));

        // Permite alterar qualquer dado caso tenha inserido errado
        meta.setNome(dto.nome());
        meta.setValorObjetivo(dto.valorObjetivo());
        if (dto.valorAcumulado() != null) {
            meta.setValorAcumulado(dto.valorAcumulado());
        }
        meta.setDataLimite(dto.dataLimite());
        meta.setCorHex(dto.corHex());

        return toResponseDTO(repository.save(meta));
    }

    @Transactional
    public MetaResponseDTO ajustarSaldo(Long id, AporteRequestDTO dto) {
        MetaFinanceira meta = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meta não encontrada com ID: " + id));

        BigDecimal valorOperacao = dto.valor();

        // 1. Se for Aporte (guardando dinheiro na meta):
        if (valorOperacao.compareTo(BigDecimal.ZERO) > 0) {
            // Busca o saldo atual que você tem disponível em conta
            BigDecimal saldoDisponivel = transacaoService.calcularSaldoAtual(); 
            // Obs: caso no seu TransacaoService o método tenha outro nome (ex: getSaldoTotal(), calcularResumo().getSaldo()),
            // basta colocar o nome do método correspondente aqui.

            if (valorOperacao.compareTo(saldoDisponivel) > 0) {
                throw new IllegalStateException("Saldo insuficiente! Você possui apenas R$ " + saldoDisponivel + " disponível.");
            }
        } 
        // 2. Se for Resgate (retirando dinheiro da meta de volta para a conta):
        else if (valorOperacao.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal valorResgate = valorOperacao.abs();
            if (valorResgate.compareTo(meta.getValorAcumulado()) > 0) {
                throw new IllegalStateException("O resgate não pode ser maior que o valor já guardado na meta!");
            }
        }

        // Atualiza o acumulado da meta
        BigDecimal novoSaldoMeta = meta.getValorAcumulado().add(valorOperacao);
        meta.setValorAcumulado(novoSaldoMeta);

        return toResponseDTO(repository.save(meta));
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Meta não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }

    // Método utilitário de conversão e cálculo de métricas
    private MetaResponseDTO toResponseDTO(MetaFinanceira meta) {
        BigDecimal objetivo = meta.getValorObjetivo();
        BigDecimal acumulado = meta.getValorAcumulado();

        // Quanto falta: max(0, objetivo - acumulado)
        BigDecimal restante = objetivo.subtract(acumulado);
        if (restante.compareTo(BigDecimal.ZERO) < 0) {
            restante = BigDecimal.ZERO;
        }

        // Percentual: (acumulado / objetivo) * 100
        BigDecimal percentual = BigDecimal.ZERO;
        if (objetivo.compareTo(BigDecimal.ZERO) > 0) {
            percentual = acumulado
                    .divide(objetivo, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        boolean concluida = acumulado.compareTo(objetivo) >= 0;

        return new MetaResponseDTO(
                meta.getId(),
                meta.getNome(),
                objetivo,
                acumulado,
                restante,
                percentual,
                meta.getDataLimite(),
                meta.getCorHex(),
                concluida
        );
    }
}