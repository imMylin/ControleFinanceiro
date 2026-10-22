package com.example.controlefinanceiro.DTO.Relatorio;

import com.example.controlefinanceiro.Enum.CategoriaGasto;

import java.math.BigDecimal;

public class RelatorioCategoriaDTO {

    private CategoriaGasto categoria;
    private BigDecimal total;

    public RelatorioCategoriaDTO(CategoriaGasto categoria,  BigDecimal total) {
        this.categoria = categoria;
        this.total = total;
    }

    public CategoriaGasto getCategoria() {return categoria;}
    public BigDecimal getTotal() {return total;}
}
