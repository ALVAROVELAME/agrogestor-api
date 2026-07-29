package com.agrogestor.api.exception;


import com.agrogestor.api.dto.MensagemRespostaDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensagemRespostaDTO> tratarRuntimeException(
            RuntimeException ex
    ){


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new MensagemRespostaDTO(
                                false,
                                ex.getMessage()
                        )
                );

    }


}