package com.financeiro.financas_api.repository;

import com.financeiro.financas_api.model.ReceitaRecorrente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceitaRecorrenteRepository extends JpaRepository<ReceitaRecorrente, Long> {
    List<ReceitaRecorrente> findByAtivoTrue();
}