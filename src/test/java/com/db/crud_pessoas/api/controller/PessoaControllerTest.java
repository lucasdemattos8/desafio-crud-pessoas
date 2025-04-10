package com.db.crud_pessoas.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.domain.service.PessoaService;

public class PessoaControllerTest {

    @InjectMocks
    private PessoaController pessoaController;

    @Mock
    private PessoaService pessoaService;

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
    
}
