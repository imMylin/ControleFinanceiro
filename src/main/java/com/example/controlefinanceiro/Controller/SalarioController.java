package com.example.controlefinanceiro.Controller;

import com.example.controlefinanceiro.DTO.SalarioRequestDTO;
import com.example.controlefinanceiro.DTO.SalarioResponseDTO;
import com.example.controlefinanceiro.DTO.SaldoSalarioResponseDTO;
import com.example.controlefinanceiro.Model.Salario;
import com.example.controlefinanceiro.Repository.SalarioRepository;
import com.example.controlefinanceiro.Service.SalarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping ("/salarios")
public class SalarioController {

    private final SalarioService salarioService;

    public SalarioController(SalarioService salarioService) {this.salarioService = salarioService;}

    @GetMapping("/saldo-mensal")
    public ResponseEntity<SaldoSalarioResponseDTO> calcularSaldoMensal(
            @RequestParam LocalDate dataReferencia) {

        SaldoSalarioResponseDTO resposta = salarioService.calcularSaldoMensal(dataReferencia);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public Salario cadastrarSalario(@Valid @RequestBody SalarioRequestDTO dados) {

        return salarioService.cadastrarOuAtualizar(
                dados.getSalario(),
                dados.getDataDeReferencia()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerSalario(@PathVariable Long id){
        salarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public List<SalarioResponseDTO> listarSalarios(){
        return salarioService.listar();
    }

}
