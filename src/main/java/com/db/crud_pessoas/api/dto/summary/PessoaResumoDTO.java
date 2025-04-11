package com.db.crud_pessoas.api.dto.summary;

import java.time.LocalDate;

import com.db.crud_pessoas.domain.entity.Pessoa;

public class PessoaResumoDTO {

    private Long id;
    private String nome;
    private LocalDate dataDeNascimento;
    private String cpf;

    public PessoaResumoDTO() {
    }

    public PessoaResumoDTO(Pessoa pessoa) {
        this.id = pessoa.getId();
        this.nome = pessoa.getNome();
        this.dataDeNascimento = pessoa.getDataDeNascimento();
        this.cpf = pessoa.getCpf();
    }

    public PessoaResumoDTO(Long id, String nome, LocalDate dataDeNascimento, String cpf) {
        this.id = id;
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.cpf = cpf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
