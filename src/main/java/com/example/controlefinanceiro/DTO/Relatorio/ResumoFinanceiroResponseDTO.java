package com.example.controlefinanceiro.DTO.Relatorio;

import java.math.BigDecimal;

public class ResumoFinanceiroResponseDTO {


    private BigDecimal totalEntradas;
    private BigDecimal totalSaidas;
    private BigDecimal saldo;

    public ResumoFinanceiroResponseDTO(BigDecimal totalEntradas, BigDecimal totalSaidas, BigDecimal saldo) {
        this.totalEntradas = totalEntradas;
        this.totalSaidas = totalSaidas;
        this.saldo = saldo;
    }

    public BigDecimal getTotalEntradas() {return totalEntradas;}

    public BigDecimal getTotalSaidas() {return totalSaidas;}

    public BigDecimal getSaldo() {return saldo;}
}
