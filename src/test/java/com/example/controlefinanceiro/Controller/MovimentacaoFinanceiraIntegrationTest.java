package com.example.controlefinanceiro.Controller;

import com.example.controlefinanceiro.Repository.MovimentacaoFinanceiraRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovimentacaoFinanceiraIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovimentacaoFinanceiraRepository repository;

    @BeforeEach
    void limparBancoDeDados() {
        repository.deleteAll();
    }

    // Método auxiliar para cadastrar uma movimentação
    private Long cadastrarMovimentacao() throws Exception {

        String json = """
                {
                    "descricao": "Teste de integração",
                    "categoriaGasto": "ALIMENTACAO",
                    "tipoMovimentacao": "SAIDA",
                    "valor": 150.00,
                    "data": "2026-10-22"
                }
                """;

        MvcResult resultado = mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String resposta = resultado.getResponse().getContentAsString();

        Number id = JsonPath.read(resposta, "$.id");

        return id.longValue();
    }

    // 1. Cadastrar uma movimentação
    @Test
    void deveCadastrarMovimentacao() throws Exception {

        String json = """
                {
                    "descricao": "Teste de integração",
                    "categoriaGasto": "ALIMENTACAO",
                    "tipoMovimentacao": "SAIDA",
                    "valor": 150.00,
                    "data": "2026-10-22"
                }
                """;

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.descricao")
                        .value("Teste de integração"))
                .andExpect(jsonPath("$.valor").value(150.00));
    }

    // 2. Consultar todas as movimentações
    @Test
    void deveListarMovimentacoes() throws Exception {

        cadastrarMovimentacao();

        mockMvc.perform(get("/movimentacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descricao")
                        .value("Teste de integração"));
    }

    // 3. Consultar uma movimentação por ID
    @Test
    void deveBuscarMovimentacaoPorId() throws Exception {

        Long id = cadastrarMovimentacao();

        mockMvc.perform(get("/movimentacoes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.descricao")
                        .value("Teste de integração"));
    }

    // 4. Atualizar uma movimentação completamente
    @Test
    void deveAtualizarMovimentacao() throws Exception {

        Long id = cadastrarMovimentacao();

        String json = """
                {
                    "descricao": "Movimentação atualizada",
                    "categoriaGasto": "ALIMENTACAO",
                    "tipoMovimentacao": "SAIDA",
                    "valor": 250.00,
                    "data": "2026-10-22"
                }
                """;

        mockMvc.perform(put("/movimentacoes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.descricao")
                        .value("Movimentação atualizada"))
                .andExpect(jsonPath("$.valor").value(250.00));
    }

    // 5. Atualizar parcialmente uma movimentação
    @Test
    void deveAtualizarMovimentacaoParcialmente() throws Exception {

        Long id = cadastrarMovimentacao();

        String json = """
                {
                    "valor": 300.00
                }
                """;

        mockMvc.perform(patch("/movimentacoes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.valor").value(300.00));
    }

    // 6. Excluir uma movimentação
    @Test
    void deveExcluirMovimentacao() throws Exception {

        Long id = cadastrarMovimentacao();

        mockMvc.perform(delete("/movimentacoes/{id}", id))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/movimentacoes/{id}", id))
                .andExpect(status().isNotFound());
    }

    // 7. Rejeitar cadastro com dados inválidos
    @Test
    void deveRejeitarCadastroComValorInvalido() throws Exception {

        String json = """
                {
                    "descricao": "Teste inválido",
                    "categoriaGasto": "ALIMENTACAO",
                    "tipoMovimentacao": "SAIDA",
                    "valor": -100.00,
                    "data": "2026-10-22"
                }
                """;

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    // 8. Retornar 404 ao consultar movimentação inexistente
    @Test
    void deveRetornar404ParaMovimentacaoInexistente() throws Exception {

        mockMvc.perform(get("/movimentacoes/{id}", 999999L))
                .andExpect(status().isNotFound());
    }
}