package com.example.controlefinanceiro.Service;

import com.example.controlefinanceiro.DTO.SalarioResponseDTO;
import com.example.controlefinanceiro.DTO.SaldoSalarioResponseDTO;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import com.example.controlefinanceiro.Exception.SalarioNaoEncontradoException;
import com.example.controlefinanceiro.Model.Salario;
import com.example.controlefinanceiro.Repository.MovimentacaoFinanceiraRepository;
import com.example.controlefinanceiro.Repository.SalarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class SalarioService {

    private final SalarioRepository salarioRepository;
    private final MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

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

    public SaldoSalarioResponseDTO calcularSaldoMensal(LocalDate dataReferencia) {

        if (dataReferencia == null) {throw new IllegalArgumentException("A data de referência é obrigatória.");}

        LocalDate primeiroDia = dataReferencia.with(java.time.temporal.TemporalAdjusters.firstDayOfMonth());

        LocalDate ultimoDia = dataReferencia.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

        Optional<Salario> salarioEncontrado = buscarPorDataDeReferencia(primeiroDia);

        if (salarioEncontrado.isEmpty()) {throw new IllegalArgumentException("Não existe salário cadastrado para esse mês.");}

        Salario salario = salarioEncontrado.get();

        BigDecimal saidas = movimentacaoFinanceiraRepository.somarPorPeriodo(TipoMovimentacao.SAIDA, primeiroDia, ultimoDia);

        if (saidas == null) {saidas = BigDecimal.ZERO;}

        BigDecimal saldoRestante = salario.getValor().subtract(saidas);

        return new SaldoSalarioResponseDTO(primeiroDia, salario.getValor(), saidas, saldoRestante);
    }

    public List<SalarioResponseDTO> listar(){

        List <Salario> lista = salarioRepository.findAll();

        return lista.stream().map(salario -> new SalarioResponseDTO(salario.getId(), salario.getValor(), salario.getDataDeReferencia())).toList();
    }

    public void deletar (Long idSalario){


        if (idSalario == null || !salarioRepository.existsById(idSalario)) {
            throw new SalarioNaoEncontradoException("Salário não encontrado.");
        }

        salarioRepository.deleteById(idSalario);
    }


}
