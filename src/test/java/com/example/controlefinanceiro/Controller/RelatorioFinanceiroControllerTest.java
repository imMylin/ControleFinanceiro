package com.example.controlefinanceiro.Controller;

import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import com.example.controlefinanceiro.Model.MovimentacaoFinanceira;
import com.example.controlefinanceiro.Repository.MovimentacaoFinanceiraRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RelatorioFinanceiroIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovimentacaoFinanceiraRepository repository;

    @BeforeEach
    void limparBanco() {
        repository.deleteAll();
    }

    private void cadastrarMovimentacao(
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

        repository.save(movimentacao);
    }

    // 1. Consultar o resumo financeiro geral
    @Test
    void deveRetornarResumoFinanceiro() throws Exception {

        cadastrarMovimentacao(
                "Salário",
                CategoriaGasto.ALIMENTACAO,
                TipoMovimentacao.ENTRADA,
                "3000.00",
                LocalDate.of(2026, 10, 5));

        cadastrarMovimentacao(
                "Mercado",
                CategoriaGasto.ALIMENTACAO,
                TipoMovimentacao.SAIDA,
                "500.00",
                LocalDate.of(2026, 10, 10));

        mockMvc.perform(get("/relatorios/resumo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEntradas").value(3000.00))
                .andExpect(jsonPath("$.totalSaidas").value(500.00))
                .andExpect(jsonPath("$.saldo").value(2500.00));
    }

    // 2. Consultar os totais por categoria
    @Test
    void deveRetornarResumoPorCategoria() throws Exception {

        cadastrarMovimentacao(
                "Almoço",
                CategoriaGasto.ALIMENTACAO,
                TipoMovimentacao.SAIDA,
                "40.00",
                LocalDate.of(2026, 10, 10));

        cadastrarMovimentacao(
                "Jantar",
                CategoriaGasto.ALIMENTACAO,
                TipoMovimentacao.SAIDA,
                "60.00",
                LocalDate.of(2026, 10, 11));

        cadastrarMovimentacao(
                "Ônibus",
                CategoriaGasto.TRANSPORTE,
                TipoMovimentacao.SAIDA,
                "20.00",
                LocalDate.of(2026, 10, 12));

        mockMvc.perform(get("/relatorios/categorias")
                        .param("tipo", "SAIDA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.categoria == 'ALIMENTACAO')].total")
                        .value(org.hamcrest.Matchers.contains(100.00)))
                .andExpect(jsonPath("$[?(@.categoria == 'TRANSPORTE')].total")
                        .value(org.hamcrest.Matchers.contains(20.00)));
    }

    // 3. Consultar o resumo de um período
    @Test
    void deveRetornarResumoPorPeriodo() throws Exception {

        cadastrarMovimentacao(
                "Salário",
                CategoriaGasto.ALIMENTACAO,
                TipoMovimentacao.ENTRADA,
                "3000.00",
                LocalDate.of(2026, 10, 5));

        cadastrarMovimentacao(
                "Mercado",
                CategoriaGasto.ALIMENTACAO,
                TipoMovimentacao.SAIDA,
                "400.00",
                LocalDate.of(2026, 10, 15));

        cadastrarMovimentacao(
                "Compra de novembro",
                CategoriaGasto.ALIMENTACAO,
                TipoMovimentacao.SAIDA,
                "700.00",
                LocalDate.of(2026, 11, 5));

        mockMvc.perform(get("/relatorios/periodo")
                        .param("dataInicial", "2026-10-01")
                        .param("dataFinal", "2026-10-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entradas").value(3000.00))
                .andExpect(jsonPath("$.saidas").value(400.00))
                .andExpect(jsonPath("$.total").value(2600.00));
    }

    // 4. Consultar resumo sem movimentações
    @Test
    void deveRetornarResumoZeradoSemMovimentacoes() throws Exception {

        mockMvc.perform(get("/relatorios/resumo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEntradas").value(0))
                .andExpect(jsonPath("$.totalSaidas").value(0))
                .andExpect(jsonPath("$.saldo").value(0));
    }
}