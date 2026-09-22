package com.financeiro.financas_api.repository;

import com.financeiro.financas_api.model.Transacao;
import com.financeiro.financas_api.model.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    
    List<Transacao> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);

    
    List<Transacao> findByTipo(TipoTransacao tipo);

    
    List<Transacao> findByCategoriaId(Long categoriaId);
}