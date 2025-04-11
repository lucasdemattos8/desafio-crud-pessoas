package com.db.crud_pessoas.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.db.crud_pessoas.api.exception.dto.ExceptionRespostaDTO;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionRespostaDTO> handleIllegalArgumentException(
            IllegalArgumentException excecao, WebRequest requisicao) {

        ExceptionRespostaDTO erroDTO = new ExceptionRespostaDTO(
            LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
            "Erro de validação", excecao.getMessage(),
            requisicao.getDescription(false)
        );

        return new ResponseEntity<>(erroDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionRespostaDTO> handleEntityNotFoundException(
            EntityNotFoundException excecao, WebRequest requisicao) {

        ExceptionRespostaDTO erroDTO = new ExceptionRespostaDTO(
            LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
            "Recurso não encontrado", excecao.getMessage(),
            requisicao.getDescription(false)
        );

        return new ResponseEntity<>(erroDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionRespostaDTO> handleGlobalException(
            Exception ex, WebRequest requisicao) {

        ExceptionRespostaDTO erroDTO = new ExceptionRespostaDTO(
            LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro interno do servidor", ex.getMessage(),
            requisicao.getDescription(false)
        );
        
        return new ResponseEntity<>(erroDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}