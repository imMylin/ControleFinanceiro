package com.example.controlefinanceiro.DTO;

import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import com.example.controlefinanceiro.Model.MovimentacaoFinanceira;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimentacaoResponseDTO {

    private Long id;
    private BigDecimal valor;
    private LocalDate data;
    private TipoMovimentacao tipoMovimentacao;
    private CategoriaGasto categoriaGasto;
    private String descricao;

    public MovimentacaoResponseDTO(MovimentacaoFinanceira movimentacao) {
        this.id = movimentacao.getId();
        this.valor = movimentacao.getValor();
        this.data = movimentacao.getData();
        this.tipoMovimentacao = movimentacao.getTipo();
        this.categoriaGasto = movimentacao.getCategoria();
        this.descricao = movimentacao.getDescricao();
    }

    public Long getId() {return id;}

    public BigDecimal getValor() {return valor;}

    public LocalDate getData() {return data;}

    public TipoMovimentacao getTipoMovimentacao() {return tipoMovimentacao;}

    public CategoriaGasto getCategoriaGasto() {return categoriaGasto;}

    public String getDescricao() {return descricao;}
}
