package com.agrogestor.api.exception;

import com.agrogestor.api.dto.MensagemRespostaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensagemRespostaDTO> tratarValidacao(
            MethodArgumentNotValidException ex
    ) {
        String msg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(err -> err.getDefaultMessage())
                .orElse("Dados inválidos");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MensagemRespostaDTO(false, msg));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MensagemRespostaDTO> tratarCredenciais(
            BadCredentialsException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new MensagemRespostaDTO(false, ex.getMessage()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<MensagemRespostaDTO> tratarDesabilitado(
            DisabledException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new MensagemRespostaDTO(false, ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensagemRespostaDTO> tratarRuntime(
            RuntimeException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new MensagemRespostaDTO(false, ex.getMessage()));
    }
}