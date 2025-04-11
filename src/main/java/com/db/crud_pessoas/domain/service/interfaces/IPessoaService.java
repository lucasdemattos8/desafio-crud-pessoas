package com.db.crud_pessoas.domain.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;

public interface IPessoaService {
    Page<PessoaDTO> listarTodasPessoas(Pageable pageable);
    PessoaDTO criarPessoa(PessoaRequisicaoDTO pessoa);
    PessoaDTO atualizarPessoa(Long id, PessoaRequisicaoDTO pessoa);
    void excluirPessoa(Long id);
}
