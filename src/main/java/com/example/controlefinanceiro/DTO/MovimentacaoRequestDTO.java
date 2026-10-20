package com.example.controlefinanceiro.DTO;

import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimentacaoRequestDTO {

    @NotBlank
    private String descricao;

    @NotNull
    private CategoriaGasto categoriaGasto;

    @NotNull
    private TipoMovimentacao tipoMovimentacao;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull
    private LocalDate data;


    public String getDescricao() {return descricao;}

    public void setDescricao(String descricao) {this.descricao = descricao;}

    public CategoriaGasto getCategoriaGasto() {return categoriaGasto;}

    public void setCategoriaGasto(CategoriaGasto categoriaGasto) {this.categoriaGasto = categoriaGasto;}

    public TipoMovimentacao getTipoMovimentacao() {return tipoMovimentacao;}

    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {this.tipoMovimentacao = tipoMovimentacao;}

    public BigDecimal getValor() {return valor;}

    public void setValor(BigDecimal valor) {this.valor = valor;}

    public LocalDate getData() {return data;}

    public void setData(LocalDate data) {this.data = data;}
}
