package com.example.controlefinanceiro.Controller;

import com.example.controlefinanceiro.DTO.MovimentacaoPatchDTO;
import com.example.controlefinanceiro.DTO.MovimentacaoRequestDTO;
import com.example.controlefinanceiro.DTO.MovimentacaoResponseDTO;
import com.example.controlefinanceiro.Service.MovimentacaoFinanceiraService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimencaoController {

    private final MovimentacaoFinanceiraService movimentacaoService;

    public MovimencaoController(MovimentacaoFinanceiraService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping
    public List<MovimentacaoResponseDTO> getMovimentacoes(){
        return movimentacaoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoResponseDTO> getMovimentacaoById(@PathVariable Long id){
        return ResponseEntity.ok(movimentacaoService.buscarMovimentacaoFinanceiraPorID(id));
    }

    @PostMapping
    public MovimentacaoResponseDTO cadastrarMovimentacao(@Valid @RequestBody MovimentacaoRequestDTO movimentacaoRequestDTO){
        return movimentacaoService.cadastrar(movimentacaoRequestDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimentacaoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody MovimentacaoRequestDTO movimentacaoRequestDTO ){
        return ResponseEntity.ok(movimentacaoService.atualizar(id, movimentacaoRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id){
        movimentacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovimentacaoResponseDTO> atualizarParcial(@PathVariable Long id, @Valid @RequestBody MovimentacaoPatchDTO patchDTO ){
        return ResponseEntity.ok(movimentacaoService.atualizarParcial(id, patchDTO));
    }

}
