package com.db.crud_pessoas.domain.service;

import java.util.List;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;
import com.db.crud_pessoas.domain.service.interfaces.IPessoaService;

public class PessoaService implements IPessoaService {

    @Override
    public List<PessoaDTO> listarTodasPessoas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarTodasPessoas'");
    }

    @Override
    public PessoaDTO criarPessoa(PessoaRequisicaoDTO pessoa) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'criarPessoa'");
    }

    @Override
    public PessoaDTO atualizarPessoa(Long id, PessoaRequisicaoDTO pessoa) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarPessoa'");
    }

    @Override
    public void excluirPessoa(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'excluirPessoa'");
    }
    
}
