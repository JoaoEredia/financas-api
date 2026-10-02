package com.financeiro.financas_api.repository;

import com.financeiro.financas_api.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    Optional<Orcamento> findByCategoriaIdAndMesAndAno(Long categoriaId, Integer mes, Integer ano);
    List<Orcamento> findByMesAndAno(Integer mes, Integer ano);
}