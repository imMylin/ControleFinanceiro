package com.example.controlefinanceiro.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "salario")
public class Salario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, unique = true)
    private LocalDate dataDeReferencia;

    public Salario(BigDecimal valor, LocalDate dataDeReferencia){
        this.valor = valor;
        this.dataDeReferencia = dataDeReferencia;
    }
    public Salario(){}

    public Long getId() {return id;}

    public BigDecimal getValor() {return valor;}

    public LocalDate getDataDeReferencia() {return dataDeReferencia;}

    public void setValor(BigDecimal valor) {this.valor = valor;}

    public void setDataDeReferencia(LocalDate dataDeReferencia) {this.dataDeReferencia = dataDeReferencia;}
}
