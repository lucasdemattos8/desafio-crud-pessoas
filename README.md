# 💻 API REST de Gerenciamento de Pessoas e Endereços

Esta API implementa um CRUD completo usando **Spring Boot** para gerenciar pessoas e seus endereços, com persistência em banco de dados PostgreSQL e documentação via Swagger/OpenAPI.

## 🚀 Funcionalidades

- **Listar pessoas com paginação**
  - Retorna lista paginada de pessoas e seus endereços
  - Permite ordenação por diferentes campos
  - Personalização de página e quantidade de registros
  
- **Gerenciamento de Pessoas**
  - Criação de pessoa com validação de CPF único
  - Atualização parcial de dados (PATCH-like através do PUT)
  - Exclusão em cascata (pessoa e endereços)
  - Cálculo automático de idade

- **Gerenciamento de Endereços**
  - Múltiplos endereços por pessoa
  - Atualização em lote de endereços
  - Exclusão automática via cascade

## 📝 Especificações Técnicas

### Entidade Pessoa
```java
@Entity
@Table(name = "usuarios")
public class Pessoa {
    @Id @GeneratedValue
    private Long id;
    
    @NotNull
    private String nome;
    
    private LocalDate dataDeNascimento;
    
    @NotNull
    @Column(unique = true)
    private String cpf;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Endereco> enderecos;
}
```

### Entidade Endereço
```java
@Entity
@Table(name = "enderecos")
public class Endereco {
    @Id @GeneratedValue
    private Long id;
    private String rua;
    private Integer numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Pessoa pessoa;
}
```

## 🛠 Tecnologias Utilizadas

- **Spring Boot 3.4.4**
- **Spring Data JPA**
- **PostgreSQL**
- **Swagger/OpenAPI 2.8.6**
- **JUnit 5**
- **Maven**

## 📚 Documentação

A API está documentada usando Swagger/OpenAPI, disponível em:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 🚀 Como Executar

1. **Pré-requisitos**
   - Java 21
   - PostgreSQL
   - Maven

2. **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/time-alice.git
cd desafio-pessoas
```

3. **Configure o banco de dados**
```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

4. **Execute a aplicação**
```bash
mvn spring-boot:run
```

## 🧪 Testes

O projeto inclui testes unitários abrangentes usando JUnit 5 e Mockito:

```bash
mvn test
```

## 📄 Endpoints

- `GET /api/usuarios` - Lista paginada de pessoas
- `POST /api/usuarios` - Cria nova pessoa
- `PUT /api/usuarios/{id}` - Atualiza pessoa existente
- `DELETE /api/usuarios/{id}` - Remove pessoa e seus endereços

## ✨ Recursos Adicionais

- Tratamento global de exceções
- Validações personalizadas
- DTOs para request/response
- Paginação e ordenação
- Swagger UI integrado
