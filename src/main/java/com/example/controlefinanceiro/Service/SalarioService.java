package com.example.controlefinanceiro.Service;

import com.example.controlefinanceiro.DTO.SaldoSalarioResponseDTO;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import com.example.controlefinanceiro.Model.Salario;
import com.example.controlefinanceiro.Repository.MovimentacaoFinanceiraRepository;
import com.example.controlefinanceiro.Repository.SalarioRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

public class SalarioService {

    private SalarioRepository salarioRepository;
    private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

    public SalarioService(SalarioRepository salarioRepository,  MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository) {
        this.salarioRepository = salarioRepository;
        this.movimentacaoFinanceiraRepository = movimentacaoFinanceiraRepository;
    }

    public Salario cadastrarOuAtualizar(BigDecimal salario, LocalDate dataReferencia) {


        if (salario == null || salario.compareTo(BigDecimal.ZERO) <= 0) {throw new IllegalArgumentException(
                    "O salário deve ser maior que zero.");}

        if (dataReferencia == null) {throw new IllegalArgumentException("A data de referência é obrigatória.");}

        if (dataReferencia.getDayOfMonth() != 1) {throw new IllegalArgumentException("A data de referência deve ser o primeiro dia do mês.");}

        Optional<Salario> salarioEncontrado = salarioRepository.findByDataDeReferencia(dataReferencia);

            if(salarioEncontrado.isPresent()){
                Salario salarioAtualizado = salarioEncontrado.get();
                salarioAtualizado.setValor(salario);
                return salarioRepository.save(salarioAtualizado);
            }
            else {
                Salario novoSalario = new Salario(salario, dataReferencia);
                return salarioRepository.save(novoSalario);
            }
    }

    public Optional<Salario> buscarPorDataDeReferencia(LocalDate dataDeReferencia) {

        if (dataDeReferencia == null) {throw new IllegalArgumentException("A data de referência é obrigatória.");}

        return salarioRepository.findByDataDeReferencia(dataDeReferencia);

    }

    public SaldoSalarioResponseDTO calcularSaldoMensal(LocalDate dataReferencia){

        if (dataReferencia == null) {throw new IllegalArgumentException("A data de referência é obrigatória.");}

        Optional<Salario> salarioEncontrado = buscarPorDataDeReferencia(dataReferencia);

        if (salarioEncontrado.isEmpty()) {
            throw new IllegalArgumentException("Não existe salário cadastrado para esse mês.");
        }

        LocalDate ultimoDia = dataReferencia.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());
        LocalDate primeiroDia = dataReferencia.with(java.time.temporal.TemporalAdjusters.firstDayOfMonth());

        BigDecimal saidas = movimentacaoFinanceiraRepository.somarPorPeriodo(TipoMovimentacao.SAIDA,  primeiroDia, ultimoDia);

        if (saidas == null) {saidas = BigDecimal.ZERO;}

        Salario salario =  salarioEncontrado.get();

        BigDecimal saldoRestante = salario.getValor().subtract(saidas);

        return new SaldoSalarioResponseDTO(dataReferencia, salario.getValor(), saidas, saldoRestante );
    }

}
