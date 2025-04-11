package com.db.crud_pessoas.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.endereco.EnderecoRequisicaoDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;
import com.db.crud_pessoas.domain.service.interfaces.IPessoaService;

import jakarta.persistence.EntityNotFoundException;

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
        Page<PessoaDTO> paginaVazia = new PageImpl<>(new ArrayList<>());
        when(pessoaService.listarTodasPessoas(any(Pageable.class))).thenReturn(paginaVazia);

        ResponseEntity<Page<PessoaDTO>> resposta = pessoaController.listarTodasPessoas(0, 10, "id");

        assertNotNull(resposta);
        assertEquals(200, resposta.getStatusCode().value());
        assertTrue(resposta.getBody().isEmpty());
        verify(pessoaService).listarTodasPessoas(any(Pageable.class));
    }

    @Test
    void deveRetornarListaComUmaPessoaCadastrada() {
        PessoaDTO pessoaDTO = criarPessoaDTO();
        Page<PessoaDTO> pagina = new PageImpl<>(List.of(pessoaDTO));
        when(pessoaService.listarTodasPessoas(any(Pageable.class))).thenReturn(pagina);

        ResponseEntity<Page<PessoaDTO>> resposta = pessoaController.listarTodasPessoas(0, 10, "id");
        final PessoaDTO pessoaDTOResposta = resposta.getBody().getContent().get(0);

        assertNotNull(resposta);
        assertEquals(200, resposta.getStatusCode().value());
        assertNotNull(resposta.getBody());
        assertEquals(1, resposta.getBody().getTotalElements());
        assertEquals(pessoaDTO.getId(), pessoaDTOResposta.getId());
        assertEquals(pessoaDTO.getNome(), pessoaDTOResposta.getNome());
        verify(pessoaService).listarTodasPessoas(any(Pageable.class));
    }

    @Test
    void deveRetornarListaComMultiplasPessoas() {
        LocalDate dataDeNascimento = LocalDate.of(1990, 1, 1);
        PessoaDTO pessoa1 = new PessoaDTO(1L, "João Silva", dataDeNascimento, "12345678900", null);
        PessoaDTO pessoa2 = new PessoaDTO(2L, "Maria Santos", dataDeNascimento, "98765432100", null);
        
        Page<PessoaDTO> pagina = new PageImpl<>(List.of(pessoa1, pessoa2));
        when(pessoaService.listarTodasPessoas(any(Pageable.class))).thenReturn(pagina);

        ResponseEntity<Page<PessoaDTO>> resposta = pessoaController.listarTodasPessoas(0, 10, "nome");
        final PessoaDTO pessoaDTOResposta1 = resposta.getBody().getContent().get(0);
        final PessoaDTO pessoaDTOResposta2 = resposta.getBody().getContent().get(1);


        assertNotNull(resposta);
        assertEquals(200, resposta.getStatusCode().value());
        assertNotNull(resposta.getBody());
        assertEquals(2, resposta.getBody().getTotalElements());
        assertEquals(pessoa1.getNome(), pessoaDTOResposta1.getNome());
        assertEquals(pessoa2.getNome(), pessoaDTOResposta2.getNome());
        verify(pessoaService).listarTodasPessoas(any(Pageable.class));
    }

    @Test
    void deveCadastrarPessoaComSucesso() {
        PessoaRequisicaoDTO requisicao = criarPessoaRequisicaoDTO();
        PessoaDTO pessoaRetornada = criarPessoaDTO();
        
        when(pessoaService.criarPessoa(any(PessoaRequisicaoDTO.class)))
            .thenReturn(pessoaRetornada);

        ResponseEntity<PessoaDTO> resposta = pessoaController.cadastrarPessoa(requisicao);

        final int idadeEsperada = 35;

        assertNotNull(resposta);
        assertEquals(201, resposta.getStatusCode().value());
        assertEquals(pessoaRetornada.getId(), resposta.getBody().getId());
        assertEquals(pessoaRetornada.getNome(), resposta.getBody().getNome());
        assertEquals(idadeEsperada, resposta.getBody().getIdade());
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
    void naoDeveAtualizarELancarExcecaoQuandoIDNaoExistirEmBanco() {
        PessoaRequisicaoDTO requisicao = new PessoaRequisicaoDTO();
        final Long idASerBuscado = 1L;

        when(pessoaService.atualizarPessoa(anyLong(), any(PessoaRequisicaoDTO.class)))
            .thenThrow(new IllegalArgumentException("Pessoa não encontrada com o id " + 1L));

        Exception exception = assertThrows(IllegalArgumentException.class, 
            () -> pessoaController.atualizarPessoa(idASerBuscado, requisicao));
        assertEquals("Pessoa não encontrada com o id 1", exception.getMessage());
    }

    @Test
    void deveDeletarPessoaComSucesso() {
        final Long idASerBuscado = 1L;
        
        doNothing().when(pessoaService).excluirPessoa(anyLong());

        ResponseEntity<?> resposta = pessoaController.deletarPessoa(idASerBuscado);

        assertNotNull(resposta);
        assertEquals(204, resposta.getStatusCode().value());
        
        assertNull(resposta.getBody());

        verify(pessoaService, times(1)).excluirPessoa(anyLong());
    }

    @Test
    void naoDeveDeletarELancarExcecaoQuandoIDNaoExistirEmBanco() {
        final Long idASerBuscado = 1L;
        final String mensagemEsperada = "Pessoa não encontrada com o id " + idASerBuscado;

        doThrow(new EntityNotFoundException(mensagemEsperada))
        .when(pessoaService)
        .excluirPessoa(anyLong());

        Exception exception = assertThrows(
            EntityNotFoundException.class, 
            () -> pessoaController.deletarPessoa(idASerBuscado));

        assertNotNull(exception);
        assertEquals("Pessoa não encontrada com o id " + idASerBuscado, exception.getMessage());
        verify(pessoaService, times(1)).excluirPessoa(idASerBuscado);
    }

    private PessoaRequisicaoDTO criarPessoaRequisicaoDTO() {
        LocalDate dataDeNascimento = LocalDate.of(1990, 1, 1);  
        List<EnderecoRequisicaoDTO> listaEnderecos = Arrays.asList(criarEnderecoRequisicaoDTO());
        return new PessoaRequisicaoDTO(
            "João Silva", dataDeNascimento, "12345678900", listaEnderecos
            );
    }

    private EnderecoRequisicaoDTO criarEnderecoRequisicaoDTO() {
        return new EnderecoRequisicaoDTO(
            "Rua Teste", 123, "Bairro Teste",
            "Cidade Teste", "Estado Teste", "12345678"
            );
    }

    private PessoaDTO criarPessoaDTO() {
        LocalDate dataDeNascimento = LocalDate.of(1990, 1, 1);        
        return new PessoaDTO(
            1L, "João Silva", dataDeNascimento, "12345678900", null
            );
    }
    
}
