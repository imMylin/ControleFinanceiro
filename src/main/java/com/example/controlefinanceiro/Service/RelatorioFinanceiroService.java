package com.example.controlefinanceiro.Service;

import com.example.controlefinanceiro.DTO.Relatorio.RelatorioCategoriaDTO;
import com.example.controlefinanceiro.DTO.Relatorio.RelatorioPeriodoDTO;
import com.example.controlefinanceiro.DTO.Relatorio.ResumoFinanceiroResponseDTO;
import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import com.example.controlefinanceiro.Repository.MovimentacaoFinanceiraRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelatorioFinanceiroService {

    private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

    public RelatorioFinanceiroService(
            MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository) {
        this.movimentacaoFinanceiraRepository = movimentacaoFinanceiraRepository;
    }

    public ResumoFinanceiroResponseDTO obterResumo(){

        BigDecimal entradas = movimentacaoFinanceiraRepository.somarPorTipo(TipoMovimentacao.ENTRADA);

        BigDecimal saidas = movimentacaoFinanceiraRepository.somarPorTipo(TipoMovimentacao.SAIDA);

        if(entradas == null){
            entradas = BigDecimal.ZERO;
        }

        if(saidas == null){
            saidas = BigDecimal.ZERO;
        }

        BigDecimal saldo = entradas.subtract(saidas);

        return new ResumoFinanceiroResponseDTO(entradas,saidas,saldo);
    }

    public List<RelatorioCategoriaDTO> obterResumoPorCategoria(TipoMovimentacao tipo){
        List <Object[]> resultados = movimentacaoFinanceiraRepository.somarPorCategoria(tipo);

        List<RelatorioCategoriaDTO> lista = new ArrayList<>();
        for (Object[] resultado : resultados) {
            CategoriaGasto categoria = (CategoriaGasto) resultado[0];
            BigDecimal total = (BigDecimal) resultado[1];

            RelatorioCategoriaDTO dto = new RelatorioCategoriaDTO(categoria,total);
            lista.add(dto);
        }
        return lista;
    }

    public RelatorioPeriodoDTO obterResumoPorPeriodo(LocalDate data_inicial, LocalDate data_final){

        if (data_inicial.isAfter(data_final)) {
            throw new IllegalArgumentException(
                    "A data inicial não pode ser posterior à data final."
            );
        }


        if(data_inicial == null){data_inicial = LocalDate.now();}
        if(data_final == null){data_final = LocalDate.now();}

        BigDecimal entradas = movimentacaoFinanceiraRepository.somarPorPeriodo(TipoMovimentacao.ENTRADA, data_inicial, data_final);
        BigDecimal saidas = movimentacaoFinanceiraRepository.somarPorPeriodo(TipoMovimentacao.SAIDA, data_inicial, data_final);

        if(entradas == null){entradas = BigDecimal.ZERO;}
        if(saidas == null){saidas = BigDecimal.ZERO;}

        BigDecimal saldo = entradas.subtract(saidas);

        return new RelatorioPeriodoDTO(entradas,saidas,saldo);

    }

}
