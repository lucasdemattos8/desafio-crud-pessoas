package com.db.crud_pessoas.api.dto;

import com.db.crud_pessoas.api.dto.summary.PessoaResumoDTO;
import com.db.crud_pessoas.domain.entity.Endereco;
import com.db.crud_pessoas.domain.entity.Pessoa;

public class EnderecoDTO {

    private Long id;
    private String rua;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    private PessoaResumoDTO pessoa;

    public EnderecoDTO(Endereco endereco) {
        this.id = endereco.getId();
        this.rua = endereco.getRua();
        this.numero = endereco.getNumero();
        this.bairro = endereco.getBairro();
        this.cidade = endereco.getCidade();
        this.estado = endereco.getEstado();
        this.cep = endereco.getCep();
        this.pessoa = toPessoaResumoDTO(endereco.getPessoa());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public PessoaResumoDTO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaResumoDTO pessoa) {
        this.pessoa = pessoa;
    }    

    private static PessoaResumoDTO toPessoaResumoDTO(Pessoa pessoa) {
        return pessoa != null ? new PessoaResumoDTO(pessoa) : null;
    }
}
