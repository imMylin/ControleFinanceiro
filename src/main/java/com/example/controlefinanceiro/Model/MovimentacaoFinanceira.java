package com.example.controlefinanceiro.Model;

import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "movimentacao_financeira")
public class MovimentacaoFinanceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaGasto categoria;

    public BigDecimal getValor() {return valor;}

    public void setValor(BigDecimal valor) {this.valor = valor;}

    public LocalDate getData() {return data;}

    public void setData(LocalDate data) {this.data = data;}

    public TipoMovimentacao getTipo() {return tipo;}

    public void setTipo(TipoMovimentacao tipo) {this.tipo = tipo;}

    public String getDescricao() {return descricao;}

    public void setDescricao(String descricao) {this.descricao = descricao;}

    public CategoriaGasto getCategoria() {return categoria;}

    public void setCategoria(CategoriaGasto categoria) {this.categoria = categoria;}

    public Long getId() {return id;}
}
