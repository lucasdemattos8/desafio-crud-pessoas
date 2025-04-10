package com.db.crud_pessoas.domain.service;

import java.util.List;

import com.db.crud_pessoas.api.dto.EnderecoDTO;
import com.db.crud_pessoas.api.dto.request.endereco.EnderecoRequisicaoDTO;
import com.db.crud_pessoas.domain.service.interfaces.IEnderecoService;

public class EnderecoService implements IEnderecoService {

    @Override
    public List<EnderecoDTO> listarTodosEnderecos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listarTodosEnderecos'");
    }

    @Override
    public EnderecoDTO criarEndereco(EnderecoRequisicaoDTO endereco) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'criarEndereco'");
    }

    @Override
    public EnderecoDTO atualizarEndereco(Long id, EnderecoRequisicaoDTO endereco) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarEndereco'");
    }

    @Override
    public void excluirEndereco(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'excluirEndereco'");
    }
    
}
