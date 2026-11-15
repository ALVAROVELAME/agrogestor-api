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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MensagemRespostaDTO> tratarNaoEncontrado(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemRespostaDTO(false, ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<MensagemRespostaDTO> tratarConflito(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MensagemRespostaDTO(false, ex.getMessage()));
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<MensagemRespostaDTO> tratarIntegridade() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MensagemRespostaDTO(false, "Dados em conflito com um registro existente."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MensagemRespostaDTO> tratarArgumento(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(new MensagemRespostaDTO(false, ex.getMessage()));
    }

    @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class,
            org.springframework.web.bind.MissingServletRequestParameterException.class,
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class})
    public ResponseEntity<MensagemRespostaDTO> tratarRequisicaoInvalida(Exception ex) {
        return ResponseEntity.badRequest().body(new MensagemRespostaDTO(false, "Requisição inválida."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensagemRespostaDTO> tratarRuntime(
            Exception ex
    ) {
        if (ex instanceof org.springframework.web.ErrorResponse error) {
            return ResponseEntity.status(error.getStatusCode()).headers(error.getHeaders())
                    .body(new MensagemRespostaDTO(false, "Requisição não suportada."));
        }
        org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class).error("Falha interna ao processar requisição", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MensagemRespostaDTO(false, "Erro interno. Tente novamente mais tarde."));
    }
}
