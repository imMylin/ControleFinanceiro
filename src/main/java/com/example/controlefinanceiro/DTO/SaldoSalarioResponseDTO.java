package com.example.controlefinanceiro.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SaldoSalarioResponseDTO {

    private LocalDate dataDeReferencia;
    private BigDecimal salario;
    private BigDecimal totalDespesas;
    private BigDecimal saldoRestante;

    public SaldoSalarioResponseDTO(LocalDate dataDeReferencia, BigDecimal salario, BigDecimal totalDespesas, BigDecimal saldoRestante) {
        this.dataDeReferencia = dataDeReferencia;
        this.salario = salario;
        this.totalDespesas = totalDespesas;
        this.saldoRestante = saldoRestante;
    }
    public LocalDate getDataDeReferencia() {return dataDeReferencia;}

    public BigDecimal getSalario() {return salario;}

    public BigDecimal getTotalDespesas() {return totalDespesas;}

    public BigDecimal getSaldoRestante() {return saldoRestante;}
}
