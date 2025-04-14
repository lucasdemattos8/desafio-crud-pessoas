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

Here's the updated "How to Run" section for the README.md with Docker configuration instructions:

## 🚀 Como Executar

1. **Pré-requisitos**
   - Docker e Docker Compose
   - Java 21
   - Maven
   - Docker (Opcional)

2. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/time-alice.git
   cd desafio-pessoas
   ```

3. **Configure o arquivo .env**
   ```properties
   DB_NAME=crud_pessoas
   DB_USERNAME=postgres
   DB_PASSWORD=postgres
   PGADMIN_DEFAULT_EMAIL=admin@admin.com
   PGADMIN_DEFAULT_PASSWORD=root
   PROFILE=dev
   ```

4. **Inicie os containers Docker**
   ```bash
   docker-compose --profile dev up --build
   ```

5. **Acesse a aplicação**
   - API: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - pgAdmin: `http://localhost:5050`
     - Login com as credenciais definidas no .env

> **Nota:** Certifique-se de que as portas 8080, 5432 (PostgreSQL) e 5050 (pgAdmin) estejam disponíveis em sua máquina.

# 🐘 Acessando o pgAdmin e conectando ao serviço `postgresdb` via Docker

Este tutorial ensina como acessar o pgAdmin em um ambiente Docker e adicionar um servidor para se conectar ao serviço PostgreSQL (`postgresdb`).

---

## ✅ Pré-requisitos

- Docker e Docker Compose instalados
- Um ambiente com `docker-compose.yml` rodando com `pgadmin` e `postgresdb`
- Variáveis de ambiente configuradas corretamente (`.env`)

---

## 1. Inicie o ambiente com Docker Compose

Se ainda não iniciou os containers, use:

```bash
   docker-compose --profile dev up --build
```

---

## 2. Acesse o pgAdmin no navegador

Abra o navegador e vá para:

```
http://localhost:5050
```

![image](https://github.com/user-attachments/assets/ca0efa12-995c-4ac4-9a2b-9406a973dc97)

---

## 3. Faça login no pgAdmin

Use as credenciais definidas nas variáveis de ambiente:

- **Email:** `${PGADMIN_DEFAULT_EMAIL}`
- **Senha:** `${PGADMIN_DEFAULT_PASSWORD}`

![image](https://github.com/user-attachments/assets/f771b7ae-9217-46d7-8427-f3836e79364e)

---

## 4. Adicione um novo servidor

Depois de logado:

1. Clique com o botão direito em "**Servers**" > "**Register**" > "**Server...**"
   
![image](https://github.com/user-attachments/assets/c8594cd7-0872-47e1-b8a1-11524e86dbfa)

---

## 5. Configurar o servidor

### Aba **General**

- **Name:** `postgresdb` (ou qualquer nome que quiser)

![image](https://github.com/user-attachments/assets/c90c7914-657a-4b5d-8a07-411b0531ba91)

---

### Aba **Connection**

Preencha com as seguintes informações:

- **Host name/address:** `postgresdb`  
  > *(Este é o nome do serviço do Postgres no docker-compose, que funciona como hostname dentro da rede Docker)*
- **Port:** `5432`
- **Maintenance database:** `${DB_NAME}`  
  *(Ou `postgres` se estiver em dúvida)*
- **Username:** `${DB_USERNAME}`
- **Password:** `${DB_PASSWORD}`
- ✅ Marque a opção “Save Password”

![image](https://github.com/user-attachments/assets/493e2b5b-676d-4c35-bae2-45fe48cf23fa)

---

## 6. Concluir e conectar

Clique em **Save**.  
Se tudo estiver correto, o servidor será adicionado e aparecerá listado na sidebar.

![image](https://github.com/user-attachments/assets/b208b3ca-8628-4e1c-be8e-91be2fd39a8b)

---

## ✅ Pronto!

Agora você pode explorar as bases de dados, rodar queries e gerenciar tudo diretamente pelo pgAdmin 🎉

---

## ⚠️ Dicas e Erros Comuns

- Verifique se o pgAdmin e o Postgres estão rodando no mesmo perfil (`dev` ou `test`)
- O nome do host deve ser **igual ao nome do serviço no `docker-compose.yml`**
- Se mudar variáveis, reinicie os containers com `docker-compose down -v && docker-compose up --build`

---

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
