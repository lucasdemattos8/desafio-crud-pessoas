package com.db.crud_pessoas.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.endereco.EnderecoRequisicaoDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;
import com.db.crud_pessoas.domain.entity.Endereco;
import com.db.crud_pessoas.domain.entity.Pessoa;
import com.db.crud_pessoas.domain.repository.EnderecoRepository;
import com.db.crud_pessoas.domain.repository.PessoaRepository;

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
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João Silva");
        pessoa.setCpf("12345678900");
        pessoa.setDataDeNascimento(LocalDate.of(1990, 1, 1));
        
        List<Pessoa> listaPessoas = Arrays.asList(pessoa);
        when(pessoaRepository.findAll()).thenReturn(listaPessoas);

        List<PessoaDTO> listaResposta = pessoaService.listarTodasPessoas();

        final int tamanhoEsperado = 1;

        assertNotNull(listaResposta);
        assertEquals(tamanhoEsperado, listaResposta.size());
        
        PessoaDTO pessoaDTO = listaResposta.get(0);
        assertEquals(pessoa.getId(), pessoaDTO.getId());
        assertEquals(pessoa.getNome(), pessoaDTO.getNome());
        assertEquals(pessoa.getCpf(), pessoaDTO.getCpf());
        assertEquals(pessoa.getDataDeNascimento(), pessoaDTO.getDataDeNascimento());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverPessoas() {
        when(pessoaRepository.findAll()).thenReturn(new ArrayList<>());

        List<PessoaDTO> listaResposta = pessoaService.listarTodasPessoas();

        assertNotNull(listaResposta);
        assertTrue(listaResposta.isEmpty());
    }

    @Test
    void deveRetornarListaComMultiplasPessoas() {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1L);
        pessoa1.setNome("João Silva");
        
        Pessoa pessoa2 = new Pessoa();
        pessoa2.setId(2L);
        pessoa2.setNome("Maria Santos");
        
        List<Pessoa> listaPessoas = Arrays.asList(pessoa1, pessoa2);
        when(pessoaRepository.findAll()).thenReturn(listaPessoas);

        List<PessoaDTO> listaResposta = pessoaService.listarTodasPessoas();

        final int tamanhoEsperado = 2;

        assertNotNull(listaResposta);
        assertEquals(tamanhoEsperado, listaResposta.size());
        assertEquals(pessoa1.getNome(), listaResposta.get(0).getNome());
        assertEquals(pessoa2.getNome(), listaResposta.get(1).getNome());
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
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome(requisicao.getNome());
        pessoa.setCpf(requisicao.getCpf());
        pessoa.setDataDeNascimento(requisicao.getDataDeNascimento());
        return pessoa;
    }

    private List<Endereco> criarEnderecosDominio(List<EnderecoRequisicaoDTO> enderecosDTO) {
        return enderecosDTO.stream()
            .map(enderecoDTO -> {
                Endereco endereco = new Endereco();
                endereco.setRua(enderecoDTO.getRua());
                endereco.setNumero(enderecoDTO.getNumero());
                endereco.setBairro(enderecoDTO.getBairro());
                endereco.setCidade(enderecoDTO.getCidade());
                endereco.setEstado(enderecoDTO.getEstado());
                endereco.setCep(enderecoDTO.getCep());
                return endereco;
            })
            .collect(Collectors.toList());
    }
        
}
