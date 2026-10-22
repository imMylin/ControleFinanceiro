package com.example.controlefinanceiro;

import com.example.controlefinanceiro.Model.MovimentacaoFinanceira;
import com.example.controlefinanceiro.Repository.MovimentacaoFinanceiraRepository;
import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(
		replace = AutoConfigureTestDatabase.Replace.NONE
)
class MovimentacaoFinanceiraRepositoryTest {

	@Autowired
	private MovimentacaoFinanceiraRepository repository;

	@BeforeEach
	void limparBanco() {
		repository.deleteAll();
	}

	// Método auxiliar para cadastrar movimentações nos testes
	private MovimentacaoFinanceira criarMovimentacao(
			String descricao,
			CategoriaGasto categoria,
			TipoMovimentacao tipo,
			String valor,
			LocalDate data) {

		MovimentacaoFinanceira movimentacao =
				new MovimentacaoFinanceira();

		movimentacao.setDescricao(descricao);
		movimentacao.setCategoria(categoria);
		movimentacao.setTipo(tipo);
		movimentacao.setValor(new BigDecimal(valor));
		movimentacao.setData(data);

		return repository.save(movimentacao);
	}

	// 1. Somar todas as saídas
	@Test
	void deveSomarAsSaidas() {

		criarMovimentacao(
				"Alimentação",
				CategoriaGasto.ALIMENTACAO,
				TipoMovimentacao.SAIDA,
				"100.00",
				LocalDate.of(2026, 10, 10));

		criarMovimentacao(
				"Transporte",
				CategoriaGasto.TRANSPORTE,
				TipoMovimentacao.SAIDA,
				"50.00",
				LocalDate.of(2026, 10, 12));

		criarMovimentacao(
				"Salário",
				CategoriaGasto.ALIMENTACAO,
				TipoMovimentacao.ENTRADA,
				"3000.00",
				LocalDate.of(2026, 10, 5));

		BigDecimal total =
				repository.somarPorTipo(TipoMovimentacao.SAIDA);

		assertNotNull(total);
		assertEquals(
				0,
				total.compareTo(new BigDecimal("150.00")));
	}

	// 2. Somar valores agrupados por categoria
	@Test
	void deveSomarPorCategoria() {

		criarMovimentacao(
				"Almoço",
				CategoriaGasto.ALIMENTACAO,
				TipoMovimentacao.SAIDA,
				"40.00",
				LocalDate.of(2026, 10, 10));

		criarMovimentacao(
				"Jantar",
				CategoriaGasto.ALIMENTACAO,
				TipoMovimentacao.SAIDA,
				"60.00",
				LocalDate.of(2026, 10, 11));

		criarMovimentacao(
				"Ônibus",
				CategoriaGasto.TRANSPORTE,
				TipoMovimentacao.SAIDA,
				"20.00",
				LocalDate.of(2026, 10, 12));

		List<Object[]> resultados =
				repository.somarPorCategoria(TipoMovimentacao.SAIDA);

		assertEquals(2, resultados.size());

		BigDecimal totalAlimentacao = BigDecimal.ZERO;
		BigDecimal totalTransporte = BigDecimal.ZERO;

		for (Object[] resultado : resultados) {
			CategoriaGasto categoria = (CategoriaGasto) resultado[0];
			BigDecimal total = (BigDecimal) resultado[1];

			if (categoria == CategoriaGasto.ALIMENTACAO) {
				totalAlimentacao = total;
			} else if (categoria == CategoriaGasto.TRANSPORTE) {
				totalTransporte = total;
			}
		}

		assertEquals(
				0,
				totalAlimentacao.compareTo(new BigDecimal("100.00")));

		assertEquals(
				0,
				totalTransporte.compareTo(new BigDecimal("20.00")));
	}

	// 3. Somar movimentações dentro de um período
	@Test
	void deveSomarPorPeriodo() {

		criarMovimentacao(
				"Conta de energia",
				CategoriaGasto.MORADIA,
				TipoMovimentacao.SAIDA,
				"120.00",
				LocalDate.of(2026, 10, 5));

		criarMovimentacao(
				"Mercado",
				CategoriaGasto.ALIMENTACAO,
				TipoMovimentacao.SAIDA,
				"180.00",
				LocalDate.of(2026, 10, 15));

		criarMovimentacao(
				"Compra fora do período",
				CategoriaGasto.ALIMENTACAO,
				TipoMovimentacao.SAIDA,
				"500.00",
				LocalDate.of(2026, 11, 5));

		BigDecimal total = repository.somarPorPeriodo(
				TipoMovimentacao.SAIDA,
				LocalDate.of(2026, 10, 1),
				LocalDate.of(2026, 10, 31));

		assertNotNull(total);
		assertEquals(
				0,
				total.compareTo(new BigDecimal("300.00")));
	}

	// 4. Retornar zero quando não houver movimentações
	@Test
	void deveRetornarZeroQuandoNaoExistiremSaidas() {

		BigDecimal total =
				repository.somarPorTipo(TipoMovimentacao.SAIDA);

		assertNotNull(total);
		assertEquals(0, total.compareTo(BigDecimal.ZERO));
	}

	// 5. Retornar zero quando não houver movimentações no período
	@Test
	void deveRetornarZeroQuandoNaoExistiremMovimentacoesNoPeriodo() {

		BigDecimal total = repository.somarPorPeriodo(
				TipoMovimentacao.SAIDA,
				LocalDate.of(2026, 10, 1),
				LocalDate.of(2026, 10, 31));

		assertNotNull(total);
		assertEquals(0, total.compareTo(BigDecimal.ZERO));
	}
}