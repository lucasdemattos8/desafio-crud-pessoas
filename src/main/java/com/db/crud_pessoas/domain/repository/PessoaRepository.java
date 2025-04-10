package com.db.crud_pessoas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.db.crud_pessoas.domain.entity.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    
}
