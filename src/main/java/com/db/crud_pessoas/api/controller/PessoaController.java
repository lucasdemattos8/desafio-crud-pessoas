package com.db.crud_pessoas.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.api.dto.request.pessoa.PessoaRequisicaoDTO;
import com.db.crud_pessoas.domain.service.interfaces.IPessoaService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RequestMapping("/api/usuarios")
@RestController
public class PessoaController {

    private IPessoaService pessoaService;

    public PessoaController(IPessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public ResponseEntity<List<PessoaDTO>> listarTodasPessoas() {
        List<PessoaDTO> listaDePessoasDominio = pessoaService.listarTodasPessoas();
        return ResponseEntity.ok().body(listaDePessoasDominio);
    }

    @PostMapping
    public ResponseEntity<PessoaDTO> cadastrarPessoa(@RequestBody PessoaRequisicaoDTO requisicao) {
        PessoaDTO pessoaDTO = pessoaService.criarPessoa(requisicao);
        return ResponseEntity.status(201).body(pessoaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizarPessoa(@PathVariable Long id, @RequestBody PessoaRequisicaoDTO requisicao) {
        PessoaDTO pessoaDTO = pessoaService.atualizarPessoa(id, requisicao); 
        return ResponseEntity.ok().body(pessoaDTO);
    }
    
}
