package com.db.crud_pessoas.domain.service.interfaces;

import java.util.List;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;

public interface IPessoaService {
    List<PessoaDTO> listarTodasPessoas();
    PessoaDTO criarPessoa(PessoaRequisicaoDTO pessoa);
    PessoaDTO atualizarPessoa(Long id, PessoaRequisicaoDTO pessoa);
    void excluirPessoa(Long id);
}
