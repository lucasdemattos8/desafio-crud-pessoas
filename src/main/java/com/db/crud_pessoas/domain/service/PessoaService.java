package com.db.crud_pessoas.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;
import com.db.crud_pessoas.domain.entity.Pessoa;
import com.db.crud_pessoas.domain.repository.EnderecoRepository;
import com.db.crud_pessoas.domain.repository.PessoaRepository;
import com.db.crud_pessoas.domain.service.interfaces.IPessoaService;

@Service
public class PessoaService implements IPessoaService {

    private PessoaRepository pessoaRepository;
    private EnderecoRepository enderecoRepository;

    public PessoaService(PessoaRepository pessoaRepository, EnderecoRepository enderecoRepository) {
        this.pessoaRepository = pessoaRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public List<PessoaDTO> listarTodasPessoas() {
        List<Pessoa> listaDePessoasDominio = pessoaRepository.findAll();
        List<PessoaDTO> listaDePessoasDTO = converterListaDeDominioParaDTO(listaDePessoasDominio);
        return listaDePessoasDTO;
    }

    public PessoaDTO criarPessoa(PessoaRequisicaoDTO pessoa) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'criarPessoa'");
    }

    public PessoaDTO atualizarPessoa(Long id, PessoaRequisicaoDTO pessoa) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarPessoa'");
    }

    public void excluirPessoa(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'excluirPessoa'");
    }

    private List<PessoaDTO> converterListaDeDominioParaDTO(List<Pessoa> listaPessoas) {
        return listaPessoas.stream().map(PessoaDTO::new).collect(Collectors.toList());
    }
    
}
