package com.db.crud_pessoas.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.endereco.EnderecoRequisicaoDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;
import com.db.crud_pessoas.domain.entity.Endereco;
import com.db.crud_pessoas.domain.entity.Pessoa;
import com.db.crud_pessoas.domain.repository.EnderecoRepository;
import com.db.crud_pessoas.domain.repository.PessoaRepository;
import com.db.crud_pessoas.domain.service.interfaces.IPessoaService;

import jakarta.persistence.EntityNotFoundException;

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

    public PessoaDTO criarPessoa(PessoaRequisicaoDTO pessoaDTO) {
        validarCadastroDTO(pessoaDTO);

        Pessoa pessoaASerSalva = new Pessoa();
        pessoaASerSalva.setNome(pessoaDTO.getNome());
        pessoaASerSalva.setCpf(pessoaDTO.getCpf());
        pessoaASerSalva.setDataDeNascimento(pessoaDTO.getDataDeNascimento());
        
        Pessoa pessoaSalva = pessoaRepository.save(pessoaASerSalva);
        
        if (pessoaDTO.getEnderecos() != null) {
            List<EnderecoRequisicaoDTO> enderecosDTO = pessoaDTO.getEnderecos();
            List<Endereco> enderecos = converterListaEnderecoDeDTOParaDominio(enderecosDTO, pessoaSalva);
            
            enderecos = enderecoRepository.saveAll(enderecos);
            
            pessoaSalva.setEnderecos(enderecos);
        }
        
        return new PessoaDTO(pessoaSalva);
    }

    @Transactional
    public PessoaDTO atualizarPessoa(Long id, PessoaRequisicaoDTO pessoaRequisicaoDTO) {
        Pessoa pessoa = pessoaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com o id " + id));
        if (pessoaRequisicaoDTO.getNome() != null) {
            pessoa.setNome(pessoaRequisicaoDTO.getNome());
        }
        if (pessoaRequisicaoDTO.getCpf() != null) {
            pessoa.setCpf(pessoaRequisicaoDTO.getCpf());
        }
        if (pessoaRequisicaoDTO.getDataDeNascimento() != null) {
            pessoa.setDataDeNascimento(pessoaRequisicaoDTO.getDataDeNascimento());
        }
        if (pessoaRequisicaoDTO.getEnderecos() != null) {
            enderecoRepository.deleteAll(pessoa.getEnderecos());
            
            List<Endereco> novosEnderecos = converterListaEnderecoDeDTOParaDominio(
                pessoaRequisicaoDTO.getEnderecos(), 
                pessoa
            );
            
            novosEnderecos = enderecoRepository.saveAll(novosEnderecos);
            pessoa.setEnderecos(novosEnderecos);
        }

        Pessoa pessoaAtualizada = pessoaRepository.save(pessoa);

        return new PessoaDTO(pessoaAtualizada);
    }

    @Transactional
    public void excluirPessoa(Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new EntityNotFoundException("Pessoa não encontrada com o id " + id);
        }
        pessoaRepository.deleteById(id);
    }

    private List<PessoaDTO> converterListaDeDominioParaDTO(List<Pessoa> listaPessoas) {
        return listaPessoas.stream().map(PessoaDTO::new).collect(Collectors.toList());
    }

    private List<Endereco> converterListaEnderecoDeDTOParaDominio(List<EnderecoRequisicaoDTO> listaEnderecos, Pessoa pessoa) {
        return listaEnderecos.stream()
                .map(enderecoDTO -> {
                    Endereco endereco = new Endereco();
                    endereco.setRua(enderecoDTO.getRua());
                    endereco.setNumero(enderecoDTO.getNumero());
                    endereco.setBairro(enderecoDTO.getBairro());
                    endereco.setCidade(enderecoDTO.getCidade());
                    endereco.setEstado(enderecoDTO.getEstado());
                    endereco.setCep(enderecoDTO.getCep());
                    endereco.setPessoa(pessoa);
                    return endereco;
                })
                .collect(Collectors.toList());
    }

    private void validarCadastroDTO(PessoaRequisicaoDTO requisicaoDTO) {
        final String CPFASerSalvo = requisicaoDTO.getCpf();

        if (CPFASerSalvo == null) {
            throw new IllegalArgumentException("O campo de CPF é um campo obrigatório.");
        }
        else if (CPFASerSalvo.length() != 11) {
            throw new IllegalArgumentException("O campo de CPF deve conter 11 digitos.");
        }
        else if (pessoaRepository.existsByCpf(CPFASerSalvo)) {
            throw new IllegalArgumentException("O CPF informado já existe em sistema.");
        }
        else if (requisicaoDTO.getNome() == null) {
            throw new IllegalArgumentException("O campo de Nome é um campo obrigatório.");
        }
        else if (requisicaoDTO.getEnderecos() == null || requisicaoDTO.getEnderecos().isEmpty()) {
            throw new IllegalArgumentException("O campo de Endereço é um campo obrigatório.");
        }
    }
    
}
