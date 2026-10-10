package com.financeiro.financas_api.controller;

import com.financeiro.financas_api.dto.AporteRequestDTO;
import com.financeiro.financas_api.dto.MetaRequestDTO;
import com.financeiro.financas_api.dto.MetaResponseDTO;
import com.financeiro.financas_api.service.MetaFinanceiraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metas")
@CrossOrigin(origins = "*") // Permite acesso durante os testes locais com o Expo
public class MetaFinanceiraController {

    private final MetaFinanceiraService service;

    public MetaFinanceiraController(MetaFinanceiraService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MetaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<MetaResponseDTO> criar(@RequestBody @Valid MetaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid MetaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @PatchMapping("/{id}/aporte")
    public ResponseEntity<MetaResponseDTO> ajustarSaldo(
            @PathVariable Long id,
            @RequestBody @Valid AporteRequestDTO dto) {
        return ResponseEntity.ok(service.ajustarSaldo(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}