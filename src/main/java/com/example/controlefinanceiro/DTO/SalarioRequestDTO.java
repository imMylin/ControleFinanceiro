package com.example.controlefinanceiro.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalarioRequestDTO {

    private LocalDate dataDeReferencia;
    private BigDecimal salario;

    public LocalDate getDataDeReferencia() {
        return dataDeReferencia;
    }

    public BigDecimal getSalario() {
        return salario;
    }
}
