package com.db.crud_pessoas.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;
import com.db.crud_pessoas.domain.service.interfaces.IPessoaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RequestMapping("/api/usuarios")
@RestController
@Tag(name = "Pessoas", description = "API para gerenciamento de pessoas")
public class PessoaController {

    private IPessoaService pessoaService;

    public PessoaController(IPessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @Operation(summary = "Listar pessoas", description = "Retorna uma lista paginada de pessoas cadastradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping
    public ResponseEntity<Page<PessoaDTO>> listarTodasPessoas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<PessoaDTO> resultado = pessoaService.listarTodasPessoas(pageable);
        
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Cadastrar pessoa", description = "Cria uma nova pessoa com seus endereços, campo de CPF e nome são obrigatórios!")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pessoa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping
    public ResponseEntity<PessoaDTO> cadastrarPessoa(@RequestBody PessoaRequisicaoDTO requisicao) {
        PessoaDTO pessoaDTO = pessoaService.criarPessoa(requisicao);
        return ResponseEntity.status(201).body(pessoaDTO);
    }

    @Operation(summary = "Atualizar pessoa", description = 
    "Atualiza os dados de uma pessoa existente. Permite atualização parcial - você pode enviar " +
    "apenas os campos que deseja modificar (nome, cpf ou data de nascimento). Os campos não informados " +
    "manterão seus valores originais. Para atualizar endereços, é necessário enviar a lista completa.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizarPessoa(@PathVariable Long id, @RequestBody PessoaRequisicaoDTO requisicao) {
        PessoaDTO pessoaDTO = pessoaService.atualizarPessoa(id, requisicao); 
        return ResponseEntity.ok().body(pessoaDTO);
    }

    @Operation(summary = "Deletar pessoa", description = "Remove uma pessoa do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pessoa removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarPessoa(@PathVariable Long id) {
        pessoaService.excluirPessoa(id); 
        return ResponseEntity.noContent().build();
    }
    
}
