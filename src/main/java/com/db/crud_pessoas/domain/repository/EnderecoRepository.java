package com.db.crud_pessoas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.db.crud_pessoas.domain.entity.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    
}
