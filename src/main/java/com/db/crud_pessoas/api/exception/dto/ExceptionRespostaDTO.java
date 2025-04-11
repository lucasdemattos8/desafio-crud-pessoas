package com.db.crud_pessoas.api.exception.dto;

import java.time.LocalDateTime;

public class ExceptionRespostaDTO {
    
    private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String messagem;
    private String path;

    public ExceptionRespostaDTO(LocalDateTime timestamp, int status, String erro, String messagem, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.erro = erro;
        this.messagem = messagem;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }

    public String getMessagem() {
        return messagem;
    }

    public String getPath() {
        return path;
    }
}