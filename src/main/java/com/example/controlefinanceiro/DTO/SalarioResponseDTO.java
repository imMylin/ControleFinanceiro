package com.example.controlefinanceiro.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalarioResponseDTO {
    private Long id;
    private BigDecimal valor;
    private LocalDate dataDeReferencia;

    public SalarioResponseDTO(Long id, BigDecimal valor, LocalDate dataDeReferencia) {
        this.id = id;
        this.valor = valor;
        this.dataDeReferencia = dataDeReferencia;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataDeReferencia() {
        return dataDeReferencia;
    }
}
