package com.example.controlefinanceiro.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalarioRequestDTO {

    @NotNull(message = "A data de referência é obrigatória")
    private LocalDate dataDeReferencia;

    @NotNull(message = "O salário é obrigatório")
    @Positive(message = "O salário deve ser maior que zero")
    private BigDecimal salario;

    public LocalDate getDataDeReferencia() {
        return dataDeReferencia;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setDataDeReferencia(LocalDate dataDeReferencia) {
        this.dataDeReferencia = dataDeReferencia;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
}
