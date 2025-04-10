package com.db.crud_pessoas.api.dto.request.pessoa;

import java.time.LocalDate;

public class PessoaRequisicaoDTO {
    
    private String nome;
    private LocalDate dataDeNascimento;
    private String cpf;
    
    public PessoaRequisicaoDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(LocalDate dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}