package com.financeiro.financas_api.service;

import com.financeiro.financas_api.model.Categoria;
import com.financeiro.financas_api.model.TipoTransacao;
import com.financeiro.financas_api.model.Transacao;
import com.financeiro.financas_api.repository.CategoriaRepository;
import com.financeiro.financas_api.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CategoriaRepository categoriaRepository;

    
    public TransacaoService(TransacaoRepository transacaoRepository, CategoriaRepository categoriaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    
    @Transactional(readOnly = true)
    public List<Transacao> listarTodas() {
        return transacaoRepository.findAll();
    }

    
    @Transactional(readOnly = true)
    public Optional<Transacao> buscarPorId(Long id) {
        return transacaoRepository.findById(id);
    }

    
    @Transactional
    public Transacao salvar(Transacao transacao) {
        Long categoriaId = transacao.getCategoria().getId();
        
        
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com o ID: " + categoriaId));

        transacao.setCategoria(categoria);
        return transacaoRepository.save(transacao);
    }

    
    @Transactional
    public void eliminar(Long id) {
        if (!transacaoRepository.existsById(id)) {
            throw new IllegalArgumentException("Transação não encontrada para eliminação com o ID: " + id);
        }
        transacaoRepository.deleteById(id);
    }

    
    @Transactional(readOnly = true)
    public List<Transacao> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return transacaoRepository.findByDataBetween(inicio, fim);
    }

    
    @Transactional(readOnly = true)
    public List<Transacao> buscarPorTipo(TipoTransacao tipo) {
        return transacaoRepository.findByTipo(tipo);
    }
}