package com.db.crud_pessoas.domain.service.interfaces;

import java.util.List;

import com.db.crud_pessoas.api.dto.EnderecoDTO;
import com.db.crud_pessoas.api.dto.request.endereco.EnderecoRequisicaoDTO;

public interface IEnderecoService {
    List<EnderecoDTO> listarTodosEnderecos();
    EnderecoDTO criarEndereco(EnderecoRequisicaoDTO endereco);
    EnderecoDTO atualizarEndereco(Long id, EnderecoRequisicaoDTO endereco);
    void excluirEndereco(Long id);
}
