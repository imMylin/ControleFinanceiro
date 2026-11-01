package com.example.controlefinanceiro.Exception;

import com.example.controlefinanceiro.DTO.ErroResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage()
                )
                .reduce((a, b) -> a + "; " + b)
                .orElse("Dados inválidos");

        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Dados inválidos",
                mensagem,
                request.getRequestURI()
        );

        return ResponseEntity
                .badRequest()
                .body(erro);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDTO> tratarArgumentoInvalido(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Requisição inválida",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .badRequest()
                .body(erro);
    }

    @ExceptionHandler(SalarioNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> tratarSalarioNaoEncontrado(SalarioNaoEncontradoException ex, HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Salario não encontrado",
                ex.getMessage(),
                request.getRequestURI());

                return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erro);
    }

    @ExceptionHandler(MovimentacaoNaoEncontrada.class)
    public ResponseEntity<ErroResponseDTO> tratarMovimentacaoNaoEncontrada(
            MovimentacaoNaoEncontrada ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Movimentação não encontrada",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDTO> tratarJsonInvalido(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Requisição inválida",
                "Os dados enviados possuem formato ou tipo inválido.",
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponseDTO> tratarParametroInvalido(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Parâmetro inválido",
                "O parâmetro '" + ex.getName()
                        + "' possui formato ou valor inválido.",
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(erro);
    }

}
