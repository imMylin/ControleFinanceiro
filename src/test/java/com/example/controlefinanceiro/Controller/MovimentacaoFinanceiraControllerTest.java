
package com.example.controlefinanceiro.Controller;

import com.example.controlefinanceiro.DTO.MovimentacaoPatchDTO;
import com.example.controlefinanceiro.DTO.MovimentacaoRequestDTO;
import com.example.controlefinanceiro.DTO.MovimentacaoResponseDTO;
import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import com.example.controlefinanceiro.Exception.MovimentacaoNaoEncontrada;
import com.example.controlefinanceiro.Service.MovimentacaoFinanceiraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(MovimencaoController.class)
class MovimentacaoFinanceiraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @MockitoBean
    private MovimentacaoFinanceiraService movimentacaoService;

    private MovimentacaoResponseDTO criarResponse() {
        return new MovimentacaoResponseDTO(
                criarEntidade()
        );
    }

    private com.example.controlefinanceiro.Model.MovimentacaoFinanceira
    criarEntidade() {
        var movimentacao =
                new com.example.controlefinanceiro.Model.MovimentacaoFinanceira();

        movimentacao.setValor(new BigDecimal("150.00"));
        movimentacao.setData(LocalDate.of(2026, 10, 22));
        movimentacao.setDescricao("Compra de supermercado");
        movimentacao.setTipo(TipoMovimentacao.SAIDA);
        movimentacao.setCategoria(CategoriaGasto.ALIMENTACAO);

        return movimentacao;
    }

    private MovimentacaoRequestDTO criarRequest() {
        MovimentacaoRequestDTO request = new MovimentacaoRequestDTO();

        request.setValor(new BigDecimal("150.00"));
        request.setData(LocalDate.of(2026, 10, 22));
        request.setDescricao("Compra de supermercado");
        request.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        request.setCategoriaGasto(CategoriaGasto.ALIMENTACAO);

        return request;
    }

    // 1. GET /movimentacoes

    @Test
    void deveListarMovimentacoes() throws Exception {
        when(movimentacaoService.listar())
                .thenReturn(List.of(criarResponse()));

        mockMvc.perform(get("/movimentacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].descricao")
                        .value("Compra de supermercado"));
    }

    // 2. GET /movimentacoes/{id}

    @Test
    void deveBuscarMovimentacaoPorId() throws Exception {
        when(movimentacaoService.buscarMovimentacaoFinanceiraPorID(1L))
                .thenReturn(criarResponse());

        mockMvc.perform(get("/movimentacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao")
                        .value("Compra de supermercado"))
                .andExpect(jsonPath("$.valor").value(150.00));
    }

    @Test
    void deveRetornarNotFoundQuandoMovimentacaoNaoExistir()
            throws Exception {
        when(movimentacaoService.buscarMovimentacaoFinanceiraPorID(99L))
                .thenThrow(new MovimentacaoNaoEncontrada(
                        "Movimentação não encontrada"));

        mockMvc.perform(get("/movimentacoes/99"))
                .andExpect(status().isNotFound());
    }

    // 3. POST /movimentacoes

    @Test
    void deveCadastrarMovimentacao() throws Exception {
        when(movimentacaoService.cadastrar(any(MovimentacaoRequestDTO.class)))
                .thenReturn(criarResponse());

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao")
                        .value("Compra de supermercado"))
                .andExpect(jsonPath("$.valor").value(150.00));
    }

    // 4. PUT /movimentacoes/{id}

    @Test
    void deveAtualizarMovimentacao() throws Exception {
        when(movimentacaoService.atualizar(
                eq(1L), any(MovimentacaoRequestDTO.class)))
                .thenReturn(criarResponse());

        mockMvc.perform(put("/movimentacoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao")
                        .value("Compra de supermercado"));
    }

    // 5. PATCH /movimentacoes/{id}

    @Test
    void deveAtualizarMovimentacaoParcialmente() throws Exception {
        MovimentacaoPatchDTO patch = new MovimentacaoPatchDTO();
        patch.setValor(new BigDecimal("200.00"));

        when(movimentacaoService.atualizarParcial(
                eq(1L), any(MovimentacaoPatchDTO.class)))
                .thenReturn(criarResponse());

        mockMvc.perform(patch("/movimentacoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao")
                        .value("Compra de supermercado"));
    }

    // 6. DELETE /movimentacoes/{id}

    @Test
    void deveExcluirMovimentacao() throws Exception {
        doNothing().when(movimentacaoService).deletar(1L);

        mockMvc.perform(delete("/movimentacoes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundQuandoExcluirMovimentacaoInexistente()
            throws Exception {
        doThrow(new MovimentacaoNaoEncontrada(
                "Movimentação não encontrada"))
                .when(movimentacaoService).deletar(99L);

        mockMvc.perform(delete("/movimentacoes/99"))
                .andExpect(status().isNotFound());
    }
}
