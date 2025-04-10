package com.db.crud_pessoas.api.dto.request.pessoa;

import java.time.LocalDate;
import java.util.List;

import com.db.crud_pessoas.api.dto.request.endereco.EnderecoRequisicaoDTO;

public class PessoaRequisicaoDTO {
    
    private String nome;
    private LocalDate dataDeNascimento;
    private String cpf;
    private List<EnderecoRequisicaoDTO> enderecos;
    
    public PessoaRequisicaoDTO() {
    }

    public PessoaRequisicaoDTO(String nome, LocalDate dataDeNascimento, String cpf,
            List<EnderecoRequisicaoDTO> enderecos) {
        this.nome = nome;
        this.dataDeNascimento = dataDeNascimento;
        this.cpf = cpf;
        this.enderecos = enderecos;
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

    public List<EnderecoRequisicaoDTO> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoRequisicaoDTO> enderecos) {
        this.enderecos = enderecos;
    }
}