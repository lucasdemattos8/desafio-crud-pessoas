package com.db.crud_pessoas.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.endereco.EnderecoRequisicaoDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;
import com.db.crud_pessoas.domain.repository.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PessoaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PessoaRepository pessoaRepository;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        pessoaRepository.deleteAll();
    }

    @Test
    void deveCriarPessoaComSucesso() throws Exception {
        PessoaRequisicaoDTO requisicao = criarPessoaRequisicaoDTO();
        String requestBody = objectMapper.writeValueAsString(requisicao);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(requisicao.getNome()))
                .andExpect(jsonPath("$.cpf").value(requisicao.getCpf()))
                .andExpect(jsonPath("$.enderecos").isArray())
                .andExpect(jsonPath("$.enderecos[0].rua").value(requisicao.getEnderecos().get(0).getRua()));
    }

    @Test
    void naoDeveCriarPessoaComCPFNulo() throws Exception {
        PessoaRequisicaoDTO pessoa = criarPessoaRequisicaoDTO();
        pessoa.setCpf(null);
        
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messagem").value("O campo de CPF é um campo obrigatório."));
    }

    @Test
    void naoDeveCriarPessoaComCPFInvalido() throws Exception {
        PessoaRequisicaoDTO pessoa = criarPessoaRequisicaoDTO();
        pessoa.setCpf("123");
        
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messagem").value("O campo de CPF deve conter 11 digitos."));
    }

    @Test
    void naoDeveCriarPessoaComNomeNulo() throws Exception {
        PessoaRequisicaoDTO pessoa = criarPessoaRequisicaoDTO();
        pessoa.setNome(null);
        
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messagem").value("O campo de Nome é um campo obrigatório."));
    }

    @Test
    void naoDeveCriarPessoaSemEnderecos() throws Exception {
        PessoaRequisicaoDTO pessoa = criarPessoaRequisicaoDTO();
        pessoa.setEnderecos(null);
        
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messagem").value("O campo de Endereço é um campo obrigatório."));
    }

    @Test
    void deveRetornarListaPaginadaDePessoas() throws Exception {
        PessoaRequisicaoDTO pessoa1 = criarPessoaRequisicaoDTO();
        PessoaRequisicaoDTO pessoa2 = criarPessoaRequisicaoDTO();
        pessoa2.setCpf("98765432100");

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa1)));
        
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa2)));

        mockMvc.perform(get("/api/usuarios")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void deveAtualizarPessoaExistente() throws Exception {
        PessoaRequisicaoDTO pessoaOriginal = criarPessoaRequisicaoDTO();
        MvcResult resultado = mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoaOriginal)))
                .andReturn();

        PessoaDTO pessoaCriada = objectMapper.readValue(
            resultado.getResponse().getContentAsString(), 
            PessoaDTO.class
        );

        PessoaRequisicaoDTO atualizacao = new PessoaRequisicaoDTO();
        atualizacao.setNome("Nome Atualizado");

        mockMvc.perform(put("/api/usuarios/" + pessoaCriada.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.cpf").value(pessoaOriginal.getCpf()));
    }

    @Test
    void naoDeveAtualizarPessoaInexistente() throws Exception {
        PessoaRequisicaoDTO atualizacao = new PessoaRequisicaoDTO();
        atualizacao.setNome("Nome Novo");
        
        mockMvc.perform(put("/api/usuarios/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizacao)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messagem").value("Pessoa não encontrada com o id 999"));
    }

    @Test
    void deveDeletarPessoaExistente() throws Exception {
        PessoaRequisicaoDTO pessoa = criarPessoaRequisicaoDTO();
        MvcResult resultado = mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pessoa)))
                .andReturn();

        PessoaDTO pessoaCriada = objectMapper.readValue(
            resultado.getResponse().getContentAsString(), 
            PessoaDTO.class
        );

        mockMvc.perform(delete("/api/usuarios/" + pessoaCriada.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void naoDeveDeletarPessoaInexistente() throws Exception {
        mockMvc.perform(delete("/api/usuarios/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messagem").value("Pessoa não encontrada com o id 999"));
    }

    private PessoaRequisicaoDTO criarPessoaRequisicaoDTO() {
        EnderecoRequisicaoDTO endereco = new EnderecoRequisicaoDTO(
            "Rua Teste", 123, "Bairro Teste",
            "Cidade Teste", "RS", "12345678"
        );

        return new PessoaRequisicaoDTO(
            "João Silva",
            LocalDate.of(1990, 1, 1),
            "12345678900",
            Arrays.asList(endereco)
        );
    }
}