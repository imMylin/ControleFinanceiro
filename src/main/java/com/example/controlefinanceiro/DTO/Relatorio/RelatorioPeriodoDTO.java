package com.example.controlefinanceiro.DTO.Relatorio;

import java.math.BigDecimal;

public class RelatorioPeriodoDTO {

    private BigDecimal entradas;
    private BigDecimal saidas;
    private BigDecimal total;

    public RelatorioPeriodoDTO(BigDecimal entradas, BigDecimal saidas, BigDecimal total) {
        this.entradas = entradas;
        this.saidas = saidas;
        this.total = total;
    }
    public BigDecimal getEntradas() {return entradas;}
    public BigDecimal getSaidas() {return saidas;}
    public BigDecimal getTotal() {return total;}

}
