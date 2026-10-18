package com.example.controlefinanceiro;

import com.example.controlefinanceiro.Model.MovimentacaoFinanceira;
import com.example.controlefinanceiro.Repository.MovimentacaoFinanceiraRepository;
import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(
		replace = AutoConfigureTestDatabase.Replace.NONE
)
class MovimentacaoFinanceiraRepositoryTest {

	@Autowired
	private MovimentacaoFinanceiraRepository repository;

	@Test
	void deveSalvarMovimentacaoFinanceira() {
		MovimentacaoFinanceira movimentacao =
				new MovimentacaoFinanceira();

		movimentacao.setValor(new BigDecimal("50.00"));
		movimentacao.setData(LocalDate.now());
		movimentacao.setTipo(TipoMovimentacao.SAIDA);
		movimentacao.setCategoria(CategoriaGasto.ALIMENTACAO);
		movimentacao.setDescricao("Almoço");

		MovimentacaoFinanceira salva =
				repository.save(movimentacao);

		assertThat(salva.getId()).isNotNull();
		assertThat(salva.getValor())
				.isEqualByComparingTo("50.00");
	}
}