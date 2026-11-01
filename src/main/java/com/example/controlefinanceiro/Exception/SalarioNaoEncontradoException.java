package com.example.controlefinanceiro.Exception;

public class SalarioNaoEncontradoException extends RuntimeException{

    public SalarioNaoEncontradoException(String mensagem){
        super(mensagem);
    }
}
