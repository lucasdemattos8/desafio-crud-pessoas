package com.db.crud_pessoas.api.dto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.db.crud_pessoas.api.dto.summary.EnderecoResumoDTO;
import com.db.crud_pessoas.domain.entity.Endereco;
import com.db.crud_pessoas.domain.entity.Pessoa;

public class PessoaDTO {

    private Long id;
    private String nome;
    private LocalDate dataDeNascimento;
    private String cpf;
    private List<EnderecoResumoDTO> enderecos;

    public PessoaDTO() {
    }

    public PessoaDTO(Pessoa pessoa) {
        this.id = pessoa.getId();
        this.nome = pessoa.getNome();
        this.dataDeNascimento = pessoa.getDataDeNascimento();
        this.cpf = pessoa.getCpf();
        this.enderecos = toEnderecosResumoDTO(pessoa.getEnderecos());
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

    public List<EnderecoResumoDTO> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoResumoDTO> enderecos) {
        this.enderecos = enderecos;
    }

    private static List<EnderecoResumoDTO> toEnderecosResumoDTO(List<Endereco> enderecos) {
        return enderecos != null 
            ? enderecos.stream()
                .map(EnderecoResumoDTO::new)
                .collect(Collectors.toList())
            : Collections.emptyList();
    }

}
