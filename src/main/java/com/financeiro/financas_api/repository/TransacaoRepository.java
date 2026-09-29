package com.financeiro.financas_api.repository;

import com.financeiro.financas_api.model.Transacao;
import com.financeiro.financas_api.model.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.financeiro.financas_api.dto.GastoPorCategoriaDTO;
import com.financeiro.financas_api.dto.EvolucaoDiariaDTO;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    
    List<Transacao> findByDataBetween(LocalDate dataInicio, LocalDate dataFim);
    @Query("SELECT new com.financeiro.financas_api.dto.GastoPorCategoriaDTO(t.categoria.nome, SUM(t.valor)) " +
           "FROM Transacao t WHERE t.tipo = 'DESPESA' AND t.data BETWEEN :inicio AND :fim " +
           "GROUP BY t.categoria.nome ORDER BY SUM(t.valor) DESC")
    List<GastoPorCategoriaDTO> somarGastosPorCategoria(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT new com.financeiro.financas_api.dto.EvolucaoDiariaDTO(t.data, SUM(t.valor)) " +
           "FROM Transacao t WHERE t.tipo = 'DESPESA' AND t.data BETWEEN :inicio AND :fim " +
           "GROUP BY t.data ORDER BY t.data ASC")
    List<EvolucaoDiariaDTO> evolucaoGastosPorDia(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t WHERE t.tipo = :tipo AND t.data BETWEEN :inicio AND :fim")
    BigDecimal somarTotalPorTipoEPeriodo(@Param("tipo") TipoTransacao tipo, @Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
    
    List<Transacao> findByTipo(TipoTransacao tipo);

    
    List<Transacao> findByCategoriaId(Long categoriaId);
}