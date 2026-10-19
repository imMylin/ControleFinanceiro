package com.example.controlefinanceiro.DTO;

import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimentacaoResponseDTO {

    private Long id;
    private BigDecimal valor;
    private LocalDate data;
    private TipoMovimentacao tipoMovimentacao;
    private CategoriaGasto categoriaGasto;
    private String descricao;

    public MovimentacaoResponseDTO(Long id, BigDecimal valor, LocalDate data, TipoMovimentacao tipoMovimentacao, CategoriaGasto categoriaGasto, String descricao) {
        this.id = id;
        this.valor = valor;
        this.data = data;
        this.tipoMovimentacao = tipoMovimentacao;
        this.categoriaGasto = categoriaGasto;
        this.descricao = descricao;
    }

    public Long getId() {return id;}

    public BigDecimal getValor() {return valor;}

    public LocalDate getData() {return data;}

    public TipoMovimentacao getTipoMovimentacao() {return tipoMovimentacao;}

    public CategoriaGasto getCategoriaGasto() {return categoriaGasto;}

    public String getDescricao() {return descricao;}
}
