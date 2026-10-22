package com.example.controlefinanceiro.Controller;

import com.example.controlefinanceiro.DTO.Relatorio.RelatorioCategoriaDTO;
import com.example.controlefinanceiro.DTO.Relatorio.RelatorioPeriodoDTO;
import com.example.controlefinanceiro.DTO.Relatorio.ResumoFinanceiroResponseDTO;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import com.example.controlefinanceiro.Service.RelatorioFinanceiroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
public class RelatorioFinanceiroController {

    private final RelatorioFinanceiroService service;

public RelatorioFinanceiroController(RelatorioFinanceiroService service) {
    this.service = service;
}

    @GetMapping("/resumo")
    public ResponseEntity<ResumoFinanceiroResponseDTO> obterResumo() {
        return ResponseEntity.ok(service.obterResumo());
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<RelatorioCategoriaDTO>> obterResumoPorCategorias(@RequestParam TipoMovimentacao tipo) {
        return ResponseEntity.ok(service.obterResumoPorCategoria(tipo));
    }

    @GetMapping("/periodo")
    public ResponseEntity<RelatorioPeriodoDTO> obterResumoPorPeriodo(@RequestParam LocalDate dataInicial, @RequestParam LocalDate dataFinal) {
        return ResponseEntity.ok(service.obterResumoPorPeriodo(dataInicial, dataFinal));
    }


}
