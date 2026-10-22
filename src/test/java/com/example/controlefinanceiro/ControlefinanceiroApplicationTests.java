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

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(
		replace = AutoConfigureTestDatabase.Replace.NONE
)
class MovimentacaoFinanceiraRepositoryTest {

	@Autowired
	private MovimentacaoFinanceiraRepository repository;

	@Test
	void deveSomarAsSaidas() {
		BigDecimal total =
				repository.somarPorTipo(TipoMovimentacao.SAIDA);

		System.out.println("Total de entradas: " + total);
	}
}