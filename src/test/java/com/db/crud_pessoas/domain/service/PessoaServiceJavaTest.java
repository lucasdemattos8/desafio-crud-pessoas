package com.db.crud_pessoas.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.domain.entity.Pessoa;
import com.db.crud_pessoas.domain.repository.PessoaRepository;

public class PessoaServiceJavaTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

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
    
}
