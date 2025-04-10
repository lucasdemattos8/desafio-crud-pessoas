# 💻 Desafio CRUD em Tempo de Execução - Pessoa e Endereço

Este projeto implementa um **CRUD** simples em **Java puro** com as entidades **Pessoa** e **Endereço**, onde os dados são mantidos **em memória** durante a execução da aplicação. Não há persistência de dados, ou seja, todas as informações serão descartadas quando a aplicação for encerrada.

## 🚀 Funcionalidades

- **Listar todas as pessoas e seus respectivos endereços**
- **Criar uma nova pessoa** com um ou mais endereços
- **Atualizar os dados de uma pessoa** e/ou seu(s) endereço(s)
- **Excluir uma pessoa** e todos os seus endereços
- **Mostrar a idade da pessoa** calculada com base na data de nascimento
- **Validações básicas** nos campos obrigatórios
- **Paginação** ao listar todas as pessoas
- **Endereço principal** da pessoa (Diferencial)

> **Nota:** Os dados são mantidos apenas **em memória** e serão **descartados** quando a aplicação for reiniciada.

## 📝 Requisitos

### Entidade **Pessoa**

- `ID` (gerado automaticamente durante a execução)
- `Nome` - **Obrigatório**
- `Data Nascimento`
- `CPF` - **Obrigatório** e único (não pode haver duplicidade)

### Entidade **Endereço**

- `ID` (gerado automaticamente durante a execução)
- `Rua`
- `Número`
- `Bairro`
- `Cidade`
- `Estado`
- `CEP`
- Relacionamento de **um-para-muitos** com a entidade **Pessoa**
- Possibilidade de indicar qual **endereço é o principal**.

## 📦 Tecnologias Utilizadas

- **Java Puro**: Linguagem de programação principal
- **JUnit**: Framework para testes unitários
- **Maven**: Gerenciamento de dependências
- **Collections (List, Map)**: Armazenamento em memória durante a execução

## ⚙️ Como Rodar o Projeto

1. **Clonar o repositório**
   ```bash
   git clone https://github.com/seu-usuario/time-alice.git
   cd desafio-crud-pessoas
