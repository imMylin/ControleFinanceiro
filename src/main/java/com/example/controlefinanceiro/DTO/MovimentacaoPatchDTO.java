package com.example.controlefinanceiro.DTO;

import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimentacaoPatchDTO {

    private LocalDate data;
    private BigDecimal valor;
    private String descricao;
    private TipoMovimentacao tipoMovimentacao;
    private CategoriaGasto categoriaGasto;

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoMovimentacao getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public CategoriaGasto getCategoriaGasto() {
        return categoriaGasto;
    }

    public void setCategoriaGasto(CategoriaGasto categoriaGasto) {
        this.categoriaGasto = categoriaGasto;
    }
}