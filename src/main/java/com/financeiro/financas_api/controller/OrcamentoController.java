package com.financeiro.financas_api.controller;

import com.financeiro.financas_api.dto.OrcamentoResumoDTO;
import com.financeiro.financas_api.model.Orcamento;
import com.financeiro.financas_api.repository.OrcamentoRepository;
import com.financeiro.financas_api.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orcamentos")
@CrossOrigin(origins = "*")
public class OrcamentoController {

    @Autowired
    private OrcamentoService orcamentoService;

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @PostMapping
    public ResponseEntity<Orcamento> salvarOuAtualizar(@RequestBody Orcamento orcamento) {
        return ResponseEntity.ok(orcamentoRepository.save(orcamento));
    }

    @GetMapping("/resumo")
    public ResponseEntity<List<OrcamentoResumoDTO>> buscarResumo(
            @RequestParam Integer mes, 
            @RequestParam Integer ano) {
        return ResponseEntity.ok(orcamentoService.obterResumoMensal(mes, ano));
    }
}