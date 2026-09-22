package com.financeiro.financas_api.controller;

import com.financeiro.financas_api.model.Transacao;
import com.financeiro.financas_api.service.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    // GET: http://localhost:8080/transacoes
    @GetMapping
    public ResponseEntity<List<Transacao>> listarTodas() {
        List<Transacao> transacoes = transacaoService.listarTodas();
        return ResponseEntity.ok(transacoes);
    }

    // GET: http://localhost:8080/transacoes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Transacao> buscarPorId(@PathVariable Long id) {
        Optional<Transacao> transacao = transacaoService.buscarPorId(id);
        return transacao.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST: http://localhost:8080/transacoes
    @PostMapping
    public ResponseEntity<Transacao> salvar(@RequestBody Transacao transacao) {
        try {
            Transacao novaTransacao = transacaoService.salvar(transacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaTransacao);
        } catch (IllegalArgumentException e) {
            // Retorna erro 400 Bad Request se a categoria não existir
            return ResponseEntity.badRequest().build(); 
        }
    }

    // DELETE: http://localhost:8080/transacoes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            transacaoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}