package com.db.crud_pessoas.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.endereco.EnderecoRequisicaoDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;
import com.db.crud_pessoas.domain.entity.Pessoa;
import com.db.crud_pessoas.domain.service.interfaces.IPessoaService;

public class PessoaControllerTest {

    @InjectMocks
    private PessoaController pessoaController;

    @Mock
    private IPessoaService pessoaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRespostaComCorpoDeListaVaziaQuandoNaoHouverPessoasCadastradas() {
        final List<PessoaDTO> listaVazia = new ArrayList<>();
        when(pessoaService.listarTodasPessoas()).thenReturn(listaVazia);

        final ResponseEntity<List<PessoaDTO>> resposta = pessoaController.listarTodasPessoas();

        assertNotNull(resposta);
        assertEquals(200, resposta.getStatusCode().value());

        assertNotNull(resposta.getBody());
        assertTrue(resposta.getBody().isEmpty());

        verify(pessoaService, times(1)).listarTodasPessoas();
    }

    @Test
    void deveRetornarListaComUmaPessoaCadastrada() {
        PessoaDTO pessoaDTO = new PessoaDTO();
        pessoaDTO.setId(1L);
        pessoaDTO.setNome("João Silva");
        pessoaDTO.setCpf("12345678900");
        
        final List<PessoaDTO> listaPessoas = List.of(pessoaDTO);
        when(pessoaService.listarTodasPessoas()).thenReturn(listaPessoas);

        ResponseEntity<List<PessoaDTO>> resposta = pessoaController.listarTodasPessoas();

        assertNotNull(resposta);
        assertEquals(200, resposta.getStatusCode().value());
        
        final List<PessoaDTO> corpoDaResposta = resposta.getBody();
        assertNotNull(corpoDaResposta);
        assertEquals(1, corpoDaResposta.size());
        
        final PessoaDTO pessoaRetornada = corpoDaResposta.get(0);
        assertEquals(pessoaDTO.getId(), pessoaRetornada.getId());
        assertEquals(pessoaDTO.getNome(), pessoaRetornada.getNome());
        assertEquals(pessoaDTO.getCpf(), pessoaRetornada.getCpf());
        
        verify(pessoaService, times(1)).listarTodasPessoas();
    }

    @Test
    void deveRetornarListaComMultiplasPessoas() {
        PessoaDTO pessoa1 = new PessoaDTO();
        pessoa1.setId(1L);
        pessoa1.setNome("João Silva");
        
        PessoaDTO pessoa2 = new PessoaDTO();
        pessoa2.setId(2L);
        pessoa2.setNome("Maria Santos");
        
        List<PessoaDTO> listaPessoas = List.of(pessoa1, pessoa2);
        when(pessoaService.listarTodasPessoas()).thenReturn(listaPessoas);

        ResponseEntity<List<PessoaDTO>> resposta = pessoaController.listarTodasPessoas();

        assertNotNull(resposta);
        assertEquals(200, resposta.getStatusCode().value());
        
        List<PessoaDTO> corpoDaResposta = resposta.getBody();
        assertNotNull(corpoDaResposta);
        assertEquals(2, corpoDaResposta.size());

        final PessoaDTO pessoaRetornada1 = corpoDaResposta.get(0);
        final PessoaDTO pessoaRetornada2 = corpoDaResposta.get(1);
        
        assertEquals(pessoa1.getId(), pessoaRetornada1.getId());
        assertEquals(pessoa1.getNome(), pessoaRetornada1.getNome());
        
        assertEquals(pessoa2.getId(), pessoaRetornada2.getId());
        assertEquals(pessoa2.getNome(), pessoaRetornada2.getNome());
        
        verify(pessoaService, times(1)).listarTodasPessoas();
    }

    @Test
    void deveCadastrarPessoaComSucesso() {
        PessoaRequisicaoDTO requisicao = criarPessoaRequisicaoDTO();
        PessoaDTO pessoaRetornada = criarPessoaDTO();
        
        when(pessoaService.criarPessoa(any(PessoaRequisicaoDTO.class)))
            .thenReturn(pessoaRetornada);

        ResponseEntity<PessoaDTO> resposta = pessoaController.cadastrarPessoa(requisicao);

        assertNotNull(resposta);
        assertEquals(201, resposta.getStatusCode().value());
        assertEquals(pessoaRetornada.getId(), resposta.getBody().getId());
        assertEquals(pessoaRetornada.getNome(), resposta.getBody().getNome());
        verify(pessoaService, times(1)).criarPessoa(any(PessoaRequisicaoDTO.class));
    }

    @Test
    void deveLancarExcecaoQuandoDadosInvalidos() {
        PessoaRequisicaoDTO requisicao = new PessoaRequisicaoDTO();
        when(pessoaService.criarPessoa(any(PessoaRequisicaoDTO.class)))
            .thenThrow(new IllegalArgumentException("Dados inválidos"));

        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> pessoaController.cadastrarPessoa(requisicao));
        assertEquals("Dados inválidos", exception.getMessage());
    }

    @Test
    void deveAtualizarPessoaComSucesso() {
        PessoaRequisicaoDTO requisicao = criarPessoaRequisicaoDTO();
        PessoaDTO pessoaRetornada = criarPessoaDTO();
        final Long idASerBuscado = 1L;
        
        when(pessoaService.atualizarPessoa(anyLong(), any(PessoaRequisicaoDTO.class)))
            .thenReturn(pessoaRetornada);

        ResponseEntity<PessoaDTO> resposta = pessoaController.atualizarPessoa(idASerBuscado, requisicao);

        assertNotNull(resposta);
        assertEquals(200, resposta.getStatusCode().value());
        assertEquals(pessoaRetornada.getId(), resposta.getBody().getId());
        assertEquals(pessoaRetornada.getNome(), resposta.getBody().getNome());
        verify(pessoaService, times(1)).atualizarPessoa(anyLong(), any(PessoaRequisicaoDTO.class));
    }

    @Test
    void deveLancarExcecaoQuandoIDNaoExistirEmBanco() {
        PessoaRequisicaoDTO requisicao = new PessoaRequisicaoDTO();
        final Long idASerBuscado = 1L;

        when(pessoaService.atualizarPessoa(anyLong(), any(PessoaRequisicaoDTO.class)))
            .thenThrow(new IllegalArgumentException("Pessoa não encontrada com o id " + 1L));

        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> pessoaController.atualizarPessoa(idASerBuscado, requisicao));
        assertEquals("Pessoa não encontrada com o id 1", exception.getMessage());
    }

    private PessoaRequisicaoDTO criarPessoaRequisicaoDTO() {
        PessoaRequisicaoDTO dto = new PessoaRequisicaoDTO();
        dto.setNome("João Silva");
        dto.setCpf("12345678900");
        dto.setDataDeNascimento(LocalDate.of(1990, 1, 1));
        dto.setEnderecos(Arrays.asList(criarEnderecoRequisicaoDTO()));
        return dto;
    }

    private EnderecoRequisicaoDTO criarEnderecoRequisicaoDTO() {
        EnderecoRequisicaoDTO endereco = new EnderecoRequisicaoDTO();
        endereco.setRua("Rua Teste");
        endereco.setNumero(123);
        endereco.setBairro("Bairro Teste");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("Estado Teste");
        endereco.setCep("12345678");
        return endereco;
    }

    private PessoaDTO criarPessoaDTO() {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João Silva");
        pessoa.setCpf("12345678900");
        pessoa.setDataDeNascimento(LocalDate.of(1990, 1, 1));
        return new PessoaDTO(pessoa);
    }
    
}
