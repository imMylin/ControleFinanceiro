package com.example.controlefinanceiro.Exception;

public class MovimentacaoNaoEncontrada extends RuntimeException{
    public MovimentacaoNaoEncontrada(String mensagem){
        super(mensagem);
    }
}
