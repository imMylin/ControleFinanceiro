package com.example.controlefinanceiro.Service;

import com.example.controlefinanceiro.DTO.MovimentacaoPatchDTO;
import com.example.controlefinanceiro.DTO.MovimentacaoRequestDTO;
import com.example.controlefinanceiro.DTO.MovimentacaoResponseDTO;
import com.example.controlefinanceiro.DTO.Relatorio.ResumoFinanceiroResponseDTO;
import com.example.controlefinanceiro.Enum.TipoMovimentacao;
import com.example.controlefinanceiro.Exception.MovimentacaoNaoEncontrada;
import com.example.controlefinanceiro.Model.MovimentacaoFinanceira;
import com.example.controlefinanceiro.Repository.MovimentacaoFinanceiraRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MovimentacaoFinanceiraService {

    private MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository;

    public MovimentacaoFinanceiraService(MovimentacaoFinanceiraRepository movimentacaoFinanceiraRepository) {
        this.movimentacaoFinanceiraRepository = movimentacaoFinanceiraRepository;
    }


    public MovimentacaoResponseDTO buscarMovimentacaoFinanceiraPorID(Long idMovimentacaoFinanceira){

        MovimentacaoFinanceira movimentacao = movimentacaoFinanceiraRepository.findById(idMovimentacaoFinanceira).orElseThrow(()
                -> new MovimentacaoNaoEncontrada("Movimentação não encontrada"));

        return new MovimentacaoResponseDTO(movimentacao);
    }

    public void deletar (Long idMovimentacaoFinanceira){
        if(!movimentacaoFinanceiraRepository.existsById(idMovimentacaoFinanceira)){
            throw new MovimentacaoNaoEncontrada("Movimentação não encontrada");
        }
        movimentacaoFinanceiraRepository.deleteById(idMovimentacaoFinanceira);
    }

    public MovimentacaoResponseDTO cadastrar(MovimentacaoRequestDTO movimentacaoFinanceira){

        MovimentacaoFinanceira movimentacao = new MovimentacaoFinanceira();

        movimentacao.setCategoria(movimentacaoFinanceira.getCategoriaGasto());
        movimentacao.setData(movimentacaoFinanceira.getData());
        movimentacao.setValor(movimentacaoFinanceira.getValor());
        movimentacao.setDescricao(movimentacaoFinanceira.getDescricao());
        movimentacao.setTipo(movimentacaoFinanceira.getTipoMovimentacao());

        movimentacao = movimentacaoFinanceiraRepository.save(movimentacao);

        return new MovimentacaoResponseDTO(movimentacao);

    }

    public MovimentacaoResponseDTO atualizar(Long id, MovimentacaoRequestDTO movimentacao){

        MovimentacaoFinanceira movimentacaoFinanceira = movimentacaoFinanceiraRepository.findById(id).orElseThrow(()->
                new MovimentacaoNaoEncontrada("Movimentação não encontrada"));

        movimentacaoFinanceira.setData(movimentacao.getData());
        movimentacaoFinanceira.setValor(movimentacao.getValor());
        movimentacaoFinanceira.setDescricao(movimentacao.getDescricao());
        movimentacaoFinanceira.setTipo(movimentacao.getTipoMovimentacao());
        movimentacaoFinanceira.setCategoria(movimentacao.getCategoriaGasto());

        MovimentacaoFinanceira movimentacaoAtualizada =  movimentacaoFinanceiraRepository.save(movimentacaoFinanceira);
        return new MovimentacaoResponseDTO(movimentacaoAtualizada);

    }

    public MovimentacaoResponseDTO atualizarParcial(Long id, MovimentacaoPatchDTO movimentacao){

        MovimentacaoFinanceira movimentacaoFinanceira = movimentacaoFinanceiraRepository.findById(id).orElseThrow(()->
                new MovimentacaoNaoEncontrada("Movimentação não encontrada"));

        if(movimentacao.getData()!=null){movimentacaoFinanceira.setData(movimentacao.getData());}

        if(movimentacao.getValor()!=null){movimentacaoFinanceira.setValor(movimentacao.getValor());}

        if(movimentacao.getDescricao()!=null){movimentacaoFinanceira.setDescricao(movimentacao.getDescricao());}

        if(movimentacao.getTipoMovimentacao()!=null){movimentacaoFinanceira.setTipo(movimentacao.getTipoMovimentacao());}

        if(movimentacao.getCategoriaGasto()!=null){movimentacaoFinanceira.setCategoria(movimentacao.getCategoriaGasto());}

        MovimentacaoFinanceira movimentacaoAtualizada =  movimentacaoFinanceiraRepository.save(movimentacaoFinanceira);
        return new MovimentacaoResponseDTO(movimentacaoAtualizada);
    }

    public List<MovimentacaoResponseDTO> listar(){

        List <MovimentacaoFinanceira> lista = movimentacaoFinanceiraRepository.findAll();

        return lista.stream().map(MovimentacaoResponseDTO::new).toList();
    }



}
