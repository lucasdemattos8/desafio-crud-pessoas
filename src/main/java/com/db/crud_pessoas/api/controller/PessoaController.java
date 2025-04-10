package com.db.crud_pessoas.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.crud_pessoas.api.dto.PessoaDTO;
import com.db.crud_pessoas.domain.service.interfaces.IPessoaService;

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
    
}
