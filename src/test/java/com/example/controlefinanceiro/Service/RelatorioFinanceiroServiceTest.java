package com.example.controlefinanceiro.Service;

import com.example.controlefinanceiro.DTO.Relatorio.RelatorioCategoriaDTO;
import com.example.controlefinanceiro.DTO.Relatorio.RelatorioPeriodoDTO;
import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Repository.MovimentacaoFinanceiraRepository;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatorioFinanceiroServiceTest {

    @Mock
    private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

    @InjectMocks
    private RelatorioFinanceiroService relatorioFinanceiroService;

    @Test
    void deveRetornarZeroQuandoNaoExistiremMovimentacoes() {

        // Arrange
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 22);

        when(movimentacaoFinanceiraRepository.somarPorPeriodo(
                TipoMovimentacao.ENTRADA, dataInicial, dataFinal))
                .thenReturn(BigDecimal.ZERO);

        when(movimentacaoFinanceiraRepository.somarPorPeriodo(
                TipoMovimentacao.SAIDA, dataInicial, dataFinal))
                .thenReturn(BigDecimal.ZERO);

        // Act
        RelatorioPeriodoDTO resultado =
                relatorioFinanceiroService.obterResumoPorPeriodo(
                        dataInicial, dataFinal);

        // Assert
        assertEquals(0, resultado.getEntradas().compareTo(BigDecimal.ZERO));
        assertEquals(0, resultado.getSaidas().compareTo(BigDecimal.ZERO));
        assertEquals(0, resultado.getTotal().compareTo(BigDecimal.ZERO));
    }

    @Test
    void deveLancarExcecaoQuandoDataInicialForPosteriorADataFinal() {

        // Arrange
        LocalDate dataInicial = LocalDate.of(2026, 10, 22);
        LocalDate dataFinal = LocalDate.of(2026, 10, 1);

        // Act + Assert
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> relatorioFinanceiroService.obterResumoPorPeriodo(
                        dataInicial, dataFinal)
        );

        assertEquals(
                "A data inicial não pode ser posterior à data final.",
                excecao.getMessage()
        );
    }

    @Test
    void deveRetornarResumoPorCategoria() {

        // Arrange
        List<Object[]> resultadosRepository = List.of(
                new Object[]{
                        CategoriaGasto.ALIMENTACAO,
                        new BigDecimal("500.00")
                },
                new Object[]{
                        CategoriaGasto.TRANSPORTE,
                        new BigDecimal("200.00")
                }
        );

        when(movimentacaoFinanceiraRepository.somarPorCategoria(
                TipoMovimentacao.SAIDA))
                .thenReturn(resultadosRepository);

        // Act
        List<RelatorioCategoriaDTO> resultado =
                relatorioFinanceiroService.obterResumoPorCategoria(
                        TipoMovimentacao.SAIDA);

        // Assert
        assertEquals(2, resultado.size());

        assertEquals(CategoriaGasto.ALIMENTACAO, resultado.get(0).getCategoria());
        assertEquals(
                0,
                resultado.get(0).getTotal().compareTo(new BigDecimal("500.00"))
        );

        assertEquals(CategoriaGasto.TRANSPORTE, resultado.get(1).getCategoria());
        assertEquals(
                0,
                resultado.get(1).getTotal().compareTo(new BigDecimal("200.00"))
        );
    }

    @Test
    void deveCalcularSaldoCorretamenteNoPeriodo() {

        // Arrange: definir os valores retornados pelo Repository
        LocalDate dataInicial = LocalDate.of(2026, 10, 1);
        LocalDate dataFinal = LocalDate.of(2026, 10, 22);

        when(movimentacaoFinanceiraRepository.somarPorPeriodo(
                TipoMovimentacao.ENTRADA, dataInicial, dataFinal))
                .thenReturn(new BigDecimal("3000.00"));

        when(movimentacaoFinanceiraRepository.somarPorPeriodo(
                TipoMovimentacao.SAIDA, dataInicial, dataFinal))
                .thenReturn(new BigDecimal("1200.00"));

        // Act: executar o método que queremos testar
        RelatorioPeriodoDTO resultado =
                relatorioFinanceiroService.obterResumoPorPeriodo(
                        dataInicial, dataFinal);

        // Assert: verificar se os valores foram calculados corretamente
        assertEquals(
                0,
                resultado.getEntradas().compareTo(new BigDecimal("3000.00"))
        );

        assertEquals(
                0,
                resultado.getSaidas().compareTo(new BigDecimal("1200.00"))
        );

        assertEquals(
                0,
                resultado.getTotal().compareTo(new BigDecimal("1800.00"))
        );
    }
    @Test
    void deveUsarDataAtualQuandoDatasForemNulas() {

        // Arrange
        LocalDate hoje = LocalDate.now();

        when(movimentacaoFinanceiraRepository.somarPorPeriodo(
                TipoMovimentacao.ENTRADA, hoje, hoje))
                .thenReturn(BigDecimal.ZERO);

        when(movimentacaoFinanceiraRepository.somarPorPeriodo(
                TipoMovimentacao.SAIDA, hoje, hoje))
                .thenReturn(BigDecimal.ZERO);

        // Act
        RelatorioPeriodoDTO resultado =
                relatorioFinanceiroService.obterResumoPorPeriodo(null, null);

        // Assert
        assertEquals(0, resultado.getEntradas().compareTo(BigDecimal.ZERO));
        assertEquals(0, resultado.getSaidas().compareTo(BigDecimal.ZERO));
        assertEquals(0, resultado.getTotal().compareTo(BigDecimal.ZERO));

        verify(movimentacaoFinanceiraRepository).somarPorPeriodo(
                TipoMovimentacao.ENTRADA, hoje, hoje);

        verify(movimentacaoFinanceiraRepository).somarPorPeriodo(
                TipoMovimentacao.SAIDA, hoje, hoje);
    }
    @Test
    void deveCalcularResumoFinanceiroGeral() {

        // Arrange
        when(movimentacaoFinanceiraRepository.somarPorTipo(
                TipoMovimentacao.ENTRADA))
                .thenReturn(new BigDecimal("3000.00"));

        when(movimentacaoFinanceiraRepository.somarPorTipo(
                TipoMovimentacao.SAIDA))
                .thenReturn(new BigDecimal("1200.00"));

        // Act
        var resultado = relatorioFinanceiroService.obterResumo();

        // Assert
        assertEquals(0,
                resultado.getTotalEntradas().compareTo(new BigDecimal("3000.00")));

        assertEquals(0,
                resultado.getTotalSaidas().compareTo(new BigDecimal("1200.00")));

        assertEquals(0,
                resultado.getSaldo().compareTo(new BigDecimal("1800.00")));
    }
}