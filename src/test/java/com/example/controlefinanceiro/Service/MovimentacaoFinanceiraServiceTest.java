
package com.example.controlefinanceiro.Service;

import com.example.controlefinanceiro.DTO.MovimentacaoPatchDTO;
import com.example.controlefinanceiro.DTO.MovimentacaoRequestDTO;
import com.example.controlefinanceiro.DTO.MovimentacaoResponseDTO;
import com.example.controlefinanceiro.Enum.CategoriaGasto;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import com.example.controlefinanceiro.Exception.MovimentacaoNaoEncontrada;
import com.example.controlefinanceiro.Model.MovimentacaoFinanceira;
import com.example.controlefinanceiro.Repository.MovimentacaoFinanceiraRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimentacaoFinanceiraServiceTest {

    @Mock
    private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

    @InjectMocks
    private MovimentacaoFinanceiraService movimentacaoFinanceiraService;

    // 1. CADASTRO

    @Test
    void deveCadastrarMovimentacaoFinanceira() {

        MovimentacaoRequestDTO request = criarRequest();

        MovimentacaoFinanceira salva = criarMovimentacao();

        when(movimentacaoFinanceiraRepository.save(
                any(MovimentacaoFinanceira.class)))
                .thenReturn(salva);

        MovimentacaoResponseDTO resultado =
                movimentacaoFinanceiraService.cadastrar(request);

        assertEquals(0, resultado.getValor()
                .compareTo(new BigDecimal("150.00")));
        assertEquals(request.getData(), resultado.getData());
        assertEquals(request.getDescricao(), resultado.getDescricao());
        assertEquals(request.getTipoMovimentacao(),
                resultado.getTipoMovimentacao());
        assertEquals(request.getCategoriaGasto(),
                resultado.getCategoriaGasto());

        verify(movimentacaoFinanceiraRepository)
                .save(any(MovimentacaoFinanceira.class));
    }

    // 2. CONSULTA POR ID

    @Test
    void deveBuscarMovimentacaoPorId() {

        Long id = 1L;
        MovimentacaoFinanceira movimentacao = criarMovimentacao();

        when(movimentacaoFinanceiraRepository.findById(id))
                .thenReturn(Optional.of(movimentacao));

        MovimentacaoResponseDTO resultado =
                movimentacaoFinanceiraService
                        .buscarMovimentacaoFinanceiraPorID(id);

        assertEquals(0, resultado.getValor()
                .compareTo(new BigDecimal("150.00")));
        assertEquals("Compra de supermercado", resultado.getDescricao());

        verify(movimentacaoFinanceiraRepository).findById(id);
    }

    @Test
    void deveLancarExcecaoQuandoConsultaPorIdNaoEncontrarMovimentacao() {

        Long id = 99L;

        when(movimentacaoFinanceiraRepository.findById(id))
                .thenReturn(Optional.empty());

        MovimentacaoNaoEncontrada excecao = assertThrows(
                MovimentacaoNaoEncontrada.class,
                () -> movimentacaoFinanceiraService
                        .buscarMovimentacaoFinanceiraPorID(id)
        );

        assertEquals("Movimentação não encontrada", excecao.getMessage());
    }

    // 3. LISTAGEM

    @Test
    void deveListarTodasAsMovimentacoes() {

        List<MovimentacaoFinanceira> movimentacoes = List.of(
                criarMovimentacao(),
                criarMovimentacao()
        );

        when(movimentacaoFinanceiraRepository.findAll())
                .thenReturn(movimentacoes);

        List<MovimentacaoResponseDTO> resultado =
                movimentacaoFinanceiraService.listar();

        assertEquals(2, resultado.size());
        assertEquals("Compra de supermercado",
                resultado.get(0).getDescricao());

        verify(movimentacaoFinanceiraRepository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremMovimentacoes() {

        when(movimentacaoFinanceiraRepository.findAll())
                .thenReturn(List.of());

        List<MovimentacaoResponseDTO> resultado =
                movimentacaoFinanceiraService.listar();

        assertEquals(0, resultado.size());
    }

    // 4. ATUALIZAÇÃO COMPLETA (PUT)

    @Test
    void deveAtualizarMovimentacaoCompleta() {

        Long id = 1L;
        MovimentacaoFinanceira existente = criarMovimentacao();

        MovimentacaoRequestDTO request = criarRequest();
        request.setValor(new BigDecimal("250.00"));
        request.setDescricao("Conta de energia");

        when(movimentacaoFinanceiraRepository.findById(id))
                .thenReturn(Optional.of(existente));

        when(movimentacaoFinanceiraRepository.save(
                any(MovimentacaoFinanceira.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        MovimentacaoResponseDTO resultado =
                movimentacaoFinanceiraService.atualizar(id, request);

        assertEquals(0, resultado.getValor()
                .compareTo(new BigDecimal("250.00")));
        assertEquals("Conta de energia", resultado.getDescricao());

        verify(movimentacaoFinanceiraRepository).save(existente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarMovimentacaoInexistente() {

        Long id = 99L;

        when(movimentacaoFinanceiraRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                MovimentacaoNaoEncontrada.class,
                () -> movimentacaoFinanceiraService.atualizar(
                        id, criarRequest())
        );
    }

    // 5. ATUALIZAÇÃO PARCIAL (PATCH)

    @Test
    void deveAtualizarMovimentacaoParcialmente() {

        Long id = 1L;
        MovimentacaoFinanceira existente = criarMovimentacao();

        MovimentacaoPatchDTO patch = new MovimentacaoPatchDTO();
        patch.setValor(new BigDecimal("300.00"));

        when(movimentacaoFinanceiraRepository.findById(id))
                .thenReturn(Optional.of(existente));

        when(movimentacaoFinanceiraRepository.save(
                any(MovimentacaoFinanceira.class)))
                .thenAnswer(invocacao -> invocacao.getArgument(0));

        MovimentacaoResponseDTO resultado =
                movimentacaoFinanceiraService.atualizarParcial(id, patch);

        assertEquals(0, resultado.getValor()
                .compareTo(new BigDecimal("300.00")));

        // Os campos que não foram enviados devem permanecer inalterados.
        assertEquals("Compra de supermercado", resultado.getDescricao());
        assertEquals(TipoMovimentacao.SAIDA,
                resultado.getTipoMovimentacao());
        assertEquals(CategoriaGasto.ALIMENTACAO,
                resultado.getCategoriaGasto());

        verify(movimentacaoFinanceiraRepository).save(existente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarParcialmenteMovimentacaoInexistente() {

        Long id = 99L;
        MovimentacaoPatchDTO patch = new MovimentacaoPatchDTO();
        patch.setValor(new BigDecimal("300.00"));

        when(movimentacaoFinanceiraRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                MovimentacaoNaoEncontrada.class,
                () -> movimentacaoFinanceiraService
                        .atualizarParcial(id, patch)
        );
    }

    // 6. EXCLUSÃO

    @Test
    void deveDeletarMovimentacaoExistente() {

        Long id = 1L;

        when(movimentacaoFinanceiraRepository.existsById(id))
                .thenReturn(true);

        movimentacaoFinanceiraService.deletar(id);

        verify(movimentacaoFinanceiraRepository).existsById(id);
        verify(movimentacaoFinanceiraRepository).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoDeletarMovimentacaoInexistente() {

        Long id = 99L;

        when(movimentacaoFinanceiraRepository.existsById(id))
                .thenReturn(false);

        assertThrows(
                MovimentacaoNaoEncontrada.class,
                () -> movimentacaoFinanceiraService.deletar(id)
        );

        verify(movimentacaoFinanceiraRepository).existsById(id);
    }

    // MÉTODOS AUXILIARES

    private MovimentacaoRequestDTO criarRequest() {

        MovimentacaoRequestDTO request = new MovimentacaoRequestDTO();

        request.setValor(new BigDecimal("150.00"));
        request.setData(LocalDate.of(2026, 10, 22));
        request.setDescricao("Compra de supermercado");
        request.setTipoMovimentacao(TipoMovimentacao.SAIDA);
        request.setCategoriaGasto(CategoriaGasto.ALIMENTACAO);

        return request;
    }

    private MovimentacaoFinanceira criarMovimentacao() {

        MovimentacaoFinanceira movimentacao =
                new MovimentacaoFinanceira();

        movimentacao.setValor(new BigDecimal("150.00"));
        movimentacao.setData(LocalDate.of(2026, 10, 22));
        movimentacao.setDescricao("Compra de supermercado");
        movimentacao.setTipo(TipoMovimentacao.SAIDA);
        movimentacao.setCategoria(CategoriaGasto.ALIMENTACAO);

        return movimentacao;
    }
}
