package com.financeiro.financas_api.service;

import com.financeiro.financas_api.model.ReceitaRecorrente;
import com.financeiro.financas_api.model.TipoRegraRecorrencia;
import com.financeiro.financas_api.model.TipoTransacao;
import com.financeiro.financas_api.model.Transacao;
import com.financeiro.financas_api.repository.ReceitaRecorrenteRepository;
import com.financeiro.financas_api.repository.TransacaoRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class ReceitaRecorrenteService {

    private final ReceitaRecorrenteRepository recorrenteRepository;
    private final TransacaoRepository transacaoRepository;

    public ReceitaRecorrenteService(ReceitaRecorrenteRepository recorrenteRepository,
                                    TransacaoRepository transacaoRepository) {
        this.recorrenteRepository = recorrenteRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void processarReceitasRecorrentes() {
        LocalDate hoje = LocalDate.now();
        String mesAtual = YearMonth.from(hoje).toString();

        List<ReceitaRecorrente> configuracoesAtivas = recorrenteRepository.findByAtivoTrue();

        for (ReceitaRecorrente regra : configuracoesAtivas) {
            if (mesAtual.equals(regra.getUltimoMesLancado())) {
                continue;
            }

            boolean deveExecutar = false;

            if (regra.getTipoRegra() == TipoRegraRecorrencia.DIA_FIXO) {
                if (regra.getDiaDoMes() != null && hoje.getDayOfMonth() >= regra.getDiaDoMes()) {
                    deveExecutar = true;
                }
            } else if (regra.getTipoRegra() == TipoRegraRecorrencia.QUINTO_DIA_UTIL) {
                if (atingiuQuintoDiaUtil(hoje)) {
                    deveExecutar = true;
                }
            }

            if (deveExecutar) {
                Transacao transacao = new Transacao();
                transacao.setDescricao(regra.getDescricao());
                transacao.setValor(regra.getValor());
                transacao.setTipo(TipoTransacao.RECEITA);
                transacao.setData(hoje);
                transacao.setCategoria(null);

                transacaoRepository.save(transacao);

                regra.setUltimoMesLancado(mesAtual);
                recorrenteRepository.save(regra);
            }
        }
    }

    private boolean atingiuQuintoDiaUtil(LocalDate data) {
        LocalDate ponteiro = data.withDayOfMonth(1);
        int diasUteis = 0;
        LocalDate dataQuintoDiaUtil = null;

        while (ponteiro.getMonth() == data.getMonth()) {
            DayOfWeek dia = ponteiro.getDayOfWeek();
            if (dia != DayOfWeek.SATURDAY && dia != DayOfWeek.SUNDAY) {
                diasUteis++;
                if (diasUteis == 5) {
                    dataQuintoDiaUtil = ponteiro;
                    break;
                }
            }
            ponteiro = ponteiro.plusDays(1);
        }

        return dataQuintoDiaUtil != null && !data.isBefore(dataQuintoDiaUtil);
    }

    public List<ReceitaRecorrente> listarTodas() {
        return recorrenteRepository.findAll();
    }

    public ReceitaRecorrente salvar(ReceitaRecorrente recorrente) {
        return recorrenteRepository.save(recorrente);
    }

    public void excluir(Long id) {
        if (!recorrenteRepository.existsById(id)) {
            throw new IllegalArgumentException("Regra recorrente não encontrada com o ID: " + id);
        }
        recorrenteRepository.deleteById(id);
    }

    public Optional<ReceitaRecorrente> buscarPorId(Long id) {
        return recorrenteRepository.findById(id);
    }
}