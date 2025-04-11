package com.db.crud_pessoas.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.endereco.EnderecoRequisicaoDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;
import com.db.crud_pessoas.domain.entity.Endereco;
import com.db.crud_pessoas.domain.entity.Pessoa;
import com.db.crud_pessoas.domain.repository.EnderecoRepository;
import com.db.crud_pessoas.domain.repository.PessoaRepository;

import jakarta.persistence.EntityNotFoundException;

public class PessoaServiceJavaTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarListaEConverterPessoaParaDTOCorretamente() {
        LocalDate dataDeAniversario = LocalDate.of(1990, 1, 1);
        Pessoa pessoa = new Pessoa(1L, "João Silva", dataDeAniversario, "12345678900", null);
        List<Pessoa> listaPessoas = List.of(pessoa);
        
        Page<Pessoa> paginaPessoas = new PageImpl<>(listaPessoas);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(pessoaRepository.findAll(any(Pageable.class))).thenReturn(paginaPessoas);

        Page<PessoaDTO> paginaResposta = pessoaService.listarTodasPessoas(pageable);
        final PessoaDTO pessoaDTOResposta = paginaResposta.getContent().get(0);

        assertNotNull(paginaResposta);
        assertEquals(1, paginaResposta.getTotalElements());
        assertEquals(pessoa.getId(), pessoaDTOResposta.getId());
        assertEquals(pessoa.getNome(), pessoaDTOResposta.getNome());
        assertEquals(pessoa.getCpf(), pessoaDTOResposta.getCpf());
        assertEquals(pessoa.getDataDeNascimento(), pessoaDTOResposta.getDataDeNascimento());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverPessoas() {
        Page<Pessoa> paginaVazia = new PageImpl<>(new ArrayList<>());
        Pageable pageable = PageRequest.of(0, 10);
        
        when(pessoaRepository.findAll(any(Pageable.class))).thenReturn(paginaVazia);

        Page<PessoaDTO> paginaResposta = pessoaService.listarTodasPessoas(pageable);

        assertNotNull(paginaResposta);
        assertTrue(paginaResposta.isEmpty());
        assertEquals(0, paginaResposta.getTotalElements());
    }

    @Test
    void deveRetornarListaComMultiplasPessoas() {
        Pessoa pessoa1 = new Pessoa(1L, "João Silva", null, "12345678900", null);
        Pessoa pessoa2 = new Pessoa(2L, "Maria Santos", null, "98765432100", null);
        
        List<Pessoa> listaPessoas = List.of(pessoa1, pessoa2);
        Page<Pessoa> paginaPessoas = new PageImpl<>(listaPessoas);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(pessoaRepository.findAll(any(Pageable.class))).thenReturn(paginaPessoas);

        Page<PessoaDTO> paginaResposta = pessoaService.listarTodasPessoas(pageable);
        final PessoaDTO pessoaDTOResposta1 = paginaResposta.getContent().get(0);
        final PessoaDTO pessoaDTOResposta2 = paginaResposta.getContent().get(1);

        assertNotNull(paginaResposta);
        assertEquals(2, paginaResposta.getTotalElements());
        assertEquals(pessoa1.getNome(), pessoaDTOResposta1.getNome());
        assertEquals(pessoa2.getNome(), pessoaDTOResposta2.getNome());
    }

    @Test
    void deveRetornarAPessoaCriadaQuandoCadastradoCorretamente() {
        PessoaRequisicaoDTO requisicao = criarPessoaRequisicaoDTO();
        Pessoa pessoaSalva = criarPessoaDominio(requisicao);
        List<Endereco> enderecosSalvos = criarEnderecosDominio(requisicao.getEnderecos());
        
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaSalva);
        when(enderecoRepository.saveAll(anyList())).thenReturn(enderecosSalvos);

        PessoaDTO resposta = pessoaService.criarPessoa(requisicao);

        assertNotNull(resposta);
        assertEquals(pessoaSalva.getId(), resposta.getId());
        assertEquals(pessoaSalva.getNome(), resposta.getNome());
        assertEquals(pessoaSalva.getCpf(), resposta.getCpf());
        assertEquals(2, resposta.getEnderecos().size());
    }

    @Test
    void deveRetornarUmaExcecaoQuandoCpfJaExistente() {
        PessoaRequisicaoDTO requisicao = criarPessoaRequisicaoDTO();
        
        when(pessoaRepository.existsByCpf(anyString())).thenReturn(true);

        Exception excecaoResposta = assertThrows(IllegalArgumentException.class, 
                () -> pessoaService.criarPessoa(requisicao));

        assertNotNull(excecaoResposta);
        assertEquals("O CPF informado já existe em sistema.", excecaoResposta.getMessage());
    }

    @Test
    void deveRetornarUmaExcecaoQuandoCpfEstiverNulo() {
        PessoaRequisicaoDTO requisicao = criarPessoaRequisicaoDTO();
        requisicao.setCpf(null);
        
        Exception excecaoResposta = assertThrows(IllegalArgumentException.class, 
                () -> pessoaService.criarPessoa(requisicao));

        assertNotNull(excecaoResposta);
        assertEquals("O campo de CPF é um campo obrigatório.", excecaoResposta.getMessage());
    }

    @Test
    void deveRetornarUmaExcecaoQuandoCpfNaoTiverOnzeDigitos() {
        PessoaRequisicaoDTO requisicao = criarPessoaRequisicaoDTO();
        requisicao.setCpf("111222333444555");
        
        Exception excecaoResposta = assertThrows(IllegalArgumentException.class, 
                () -> pessoaService.criarPessoa(requisicao));

        assertNotNull(excecaoResposta);
        assertEquals("O campo de CPF deve conter 11 digitos.", excecaoResposta.getMessage());
    }

    @Test
    void deveRetornarUmaExcecaoQuandoNomeEstiverNulo() {
        PessoaRequisicaoDTO requisicao = criarPessoaRequisicaoDTO();
        requisicao.setNome(null);
        
        Exception excecaoResposta = assertThrows(IllegalArgumentException.class, 
                () -> pessoaService.criarPessoa(requisicao));

        assertNotNull(excecaoResposta);
        assertEquals("O campo de Nome é um campo obrigatório.", excecaoResposta.getMessage());
    }

    @Test
    void deveRetornarUmaExcecaoQuandoEnderecoEstiverNulo() {
        PessoaRequisicaoDTO requisicao = criarPessoaRequisicaoDTO();
        requisicao.setEnderecos(null);
        
        Exception excecaoResposta = assertThrows(IllegalArgumentException.class, 
                () -> pessoaService.criarPessoa(requisicao));

        assertNotNull(excecaoResposta);
        assertEquals("O campo de Endereço é um campo obrigatório.", excecaoResposta.getMessage());
    }

    @Test
    void deveRetornarUmaExcecaoQuandoEnderecoSerUmaListaVazia() {
        PessoaRequisicaoDTO requisicao = criarPessoaRequisicaoDTO();
        requisicao.setEnderecos(new ArrayList<>());
        
        Exception excecaoResposta = assertThrows(IllegalArgumentException.class, 
                () -> pessoaService.criarPessoa(requisicao));

        assertNotNull(excecaoResposta);
        assertEquals("O campo de Endereço é um campo obrigatório.", excecaoResposta.getMessage());
    }

    @Test
    void deveAtualizarNomeDaPessoaQuandoInformado() {
        Long id = 1L;
        Pessoa pessoaExistente = new Pessoa(
            1L, "Nome Antigo", null, "12345678900", null);
        
        PessoaRequisicaoDTO requisicao = new PessoaRequisicaoDTO();
        requisicao.setNome("Nome Novo");
        
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaExistente);

        PessoaDTO resultado = pessoaService.atualizarPessoa(id, requisicao);

        assertNotNull(resultado);
        assertEquals("Nome Novo", resultado.getNome());
        assertEquals("12345678900", resultado.getCpf());
        verify(pessoaRepository).findById(id);
        verify(pessoaRepository).save(any(Pessoa.class));
    }

    @Test
    void naoDeveAtualizarQuandoPessoaNaoEncontrada() {
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());
        
        PessoaRequisicaoDTO requisicao = new PessoaRequisicaoDTO();
        requisicao.setNome("Nome Novo");
        
        EntityNotFoundException exception = assertThrows(
            EntityNotFoundException.class,
            () -> pessoaService.atualizarPessoa(id, requisicao)
        );
        
        assertEquals("Pessoa não encontrada com o id 1", exception.getMessage());
    }

    @Test
    void deveAtualizarEnderecosDaPessoa() {
        Long id = 1L;
        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setId(id);
        pessoaExistente.setNome("Nome Teste");
        
        List<Endereco> enderecosAntigos = Arrays.asList(
            criarEndereco("Rua Antiga", 100, "Bairro Antigo", "Cidade Antiga")
        );
        pessoaExistente.setEnderecos(enderecosAntigos);
        
        PessoaRequisicaoDTO requisicao = new PessoaRequisicaoDTO();
        EnderecoRequisicaoDTO novoEnderecoDTO = new EnderecoRequisicaoDTO(
            "Nova Rua", 200, "Novo Bairro", "Nova Cidade", "RS", "90000-000"
        );
        requisicao.setEnderecos(Arrays.asList(novoEnderecoDTO));
        
        List<Endereco> novosEnderecos = criarEnderecosDominio(requisicao.getEnderecos());
        
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));
        when(enderecoRepository.saveAll(anyList())).thenReturn(novosEnderecos);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaExistente);

        PessoaDTO resultado = pessoaService.atualizarPessoa(id, requisicao);

        assertNotNull(resultado);
        assertNotNull(resultado.getEnderecos());
        assertEquals(1, resultado.getEnderecos().size());
        assertEquals("Nova Rua", resultado.getEnderecos().get(0).getRua());
        assertEquals("Nova Cidade", resultado.getEnderecos().get(0).getCidade());
        verify(enderecoRepository).deleteAll(enderecosAntigos);
        verify(enderecoRepository).saveAll(anyList());
        verify(pessoaRepository).save(any(Pessoa.class));
    }

    @Test
    void deveManterDadosExistentesQuandoNaoInformados() {
        Long id = 1L;
        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setId(id);
        pessoaExistente.setNome("Nome Original");
        pessoaExistente.setCpf("12345678900");
        
        PessoaRequisicaoDTO requisicao = new PessoaRequisicaoDTO();
        
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaExistente);

        PessoaDTO resultado = pessoaService.atualizarPessoa(id, requisicao);
        
        assertNotNull(resultado);
        assertEquals("Nome Original", resultado.getNome());
        assertEquals("12345678900", resultado.getCpf());
    }

    @Test
    void deveExcluirPessoaQuandoExistir() {
        Long id = 1L;
        when(pessoaRepository.existsById(id)).thenReturn(true);
        doNothing().when(pessoaRepository).deleteById(id);

        pessoaService.excluirPessoa(id);

        verify(pessoaRepository).existsById(id);
        verify(pessoaRepository).deleteById(id);
    }

    @Test
    void naoDeveDeletarQuandoPessoaNaoEncontrada() {
        Long pessoaASerDeletadaID = 1L;
        when(pessoaRepository.existsById(pessoaASerDeletadaID)).thenReturn(false);

        Exception exceptionResposta = assertThrows(
            EntityNotFoundException.class,
            () -> pessoaService.excluirPessoa(pessoaASerDeletadaID));
        
        assertEquals("Pessoa não encontrada com o id " + pessoaASerDeletadaID, exceptionResposta.getMessage());
        verify(pessoaRepository).existsById(pessoaASerDeletadaID);
        verify(pessoaRepository, never()).deleteById(anyLong());
    }

    private PessoaRequisicaoDTO criarPessoaRequisicaoDTO() {
        EnderecoRequisicaoDTO endereco1 = new EnderecoRequisicaoDTO(
            "Rua Platina", 123, "Santo Afonso", 
            "Novo Hamburgo", "RS", "93425-385");

        EnderecoRequisicaoDTO endereco2 = new EnderecoRequisicaoDTO(
            "Avenida Pernambuco", 321, "Navegantes",
            "Porto Alegre", "RS", "90240-000");

        return new PessoaRequisicaoDTO(
            "João Silva", 
            LocalDate.of(1990, 1, 1),
            "12345678900", 
            Arrays.asList(endereco1, endereco2));
    }

    private Pessoa criarPessoaDominio(PessoaRequisicaoDTO requisicao) {
        return new Pessoa(
            1L, requisicao.getNome(), requisicao.getDataDeNascimento(),
            requisicao.getCpf(), null);
    }

    private List<Endereco> criarEnderecosDominio(List<EnderecoRequisicaoDTO> enderecosDTO) {
        return enderecosDTO.stream()
            .map(enderecoDTO -> {
                return new Endereco(
                    null, enderecoDTO.getRua(),
                    enderecoDTO.getNumero(), enderecoDTO.getBairro(),
                    enderecoDTO.getCidade(), enderecoDTO.getEstado(),
                    enderecoDTO.getCep(), null);
            })
            .collect(Collectors.toList());
    }

    private Endereco criarEndereco(String rua, Integer numero, String bairro, String cidade) {
        return new Endereco(
            null, rua, numero, bairro,
            cidade, "RS", "90000-000",
            null);
    }
        
}
