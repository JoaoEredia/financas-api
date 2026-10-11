package com.financeiro.financas_api.controller;

import com.financeiro.financas_api.model.ReceitaRecorrente;
import com.financeiro.financas_api.service.ReceitaRecorrenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receitas-recorrentes")
@CrossOrigin(origins = "*")
public class ReceitaRecorrenteController {

    private final ReceitaRecorrenteService service;

    public ReceitaRecorrenteController(ReceitaRecorrenteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReceitaRecorrente>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @PostMapping
    public ResponseEntity<ReceitaRecorrente> salvar(@RequestBody ReceitaRecorrente recorrente) {
        ReceitaRecorrente salva = service.salvar(recorrente);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceitaRecorrente> atualizar(@PathVariable Long id, @RequestBody ReceitaRecorrente dados) {
        return service.buscarPorId(id)
                .map(existente -> {
                    existente.setDescricao(dados.getDescricao());
                    existente.setValor(dados.getValor());
                    existente.setTipoRegra(dados.getTipoRegra());
                    existente.setDiaDoMes(dados.getDiaDoMes());
                    existente.setAtivo(dados.isAtivo());
                    ReceitaRecorrente atualizada = service.salvar(existente);
                    return ResponseEntity.ok(atualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}