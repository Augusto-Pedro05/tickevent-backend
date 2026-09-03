# 🎟️ TickEvent Backend

API RESTful para gestão e emissão de ingressos de eventos, construída com **Java 25**, **Spring Boot**, **PostgreSQL** e **Docker**, seguindo os princípios da **Arquitetura Hexagonal Híbrida (Ports & Adapters)** e **Domain-Driven Design (DDD)**.

---

## 📌 Sumário
- [1. Visão Geral e Tecnologias](#1-visão-geral-e-tecnologias)
- [2. Arquitetura do Projeto](#2-arquitetura-do-projeto)
- [3. Rotas Atualmente Disponíveis (API Reference)](#3-rotas-atualmente-disponíveis-api-reference)
  - [3.1. Cadastro de Cliente](#31-cadastro-de-cliente)
  - [3.2. Cadastro de Administrador / Produtor](#32-cadastro-de-administrador--produtor)
  - [3.3. Login e Autenticação](#33-login-e-autenticação)
  - [3.4. Tratamento Global de Erros](#34-tratamento-global-de-erros)
- [4. Como Rodar 100% com Docker](#4-como-rodar-100-com-docker)
  - [Pré-requisitos](#pré-requisitos)
  - [Subindo os Containers](#subindo-os-containers)
  - [Testando via Terminal (cURL)](#testando-via-terminal-curl)
  - [Inspecionando o Banco de Dados no Container](#inspecionando-o-banco-de-dados-no-container)
  - [Troubleshooting & Dicas do Docker](#troubleshooting--dicas-do-docker)
- [5. Execução em Modo de Desenvolvimento Local](#5-execução-em-modo-de-desenvolvimento-local)
- [6. Testes Automatizados](#6-testes-automatizados)

---

## 1. Visão Geral e Tecnologias

O **TickEvent Backend** é responsável por gerenciar todo o ciclo de vida de eventos: onboarding de produtores e clientes, criação e publicação de eventos, configuração de lotes e categorias de ingressos, emissão e checkout.

### Stack Tecnológica
* **Linguagem:** Java 25 (OpenJDK)
* **Framework:** Spring Boot 4.x
* **Segurança:** Spring Security (Stateless com JWT e BCrypt)
* **Persistência:** Spring Data JPA + Hibernate
* **Banco de Dados:** PostgreSQL 16 (H2 em memória para testes)
* **Mapeamento:** MapStruct 1.6
* **Produtividade:** Lombok 1.18
* **Containerização:** Docker + Docker Compose

---

## 2. Arquitetura do Projeto

A aplicação utiliza uma **Arquitetura Hexagonal Híbrida (Pragmática)**. O modelo de domínio e as interfaces de portas são completamente isolados dos detalhes de infraestrutura e persistência, enquanto a camada de aplicação aproveita a ergonomia do Spring (`@Service` e `@Transactional`):

```
                        ┌────────────────────────────────────────────────┐
                        │               ADAPTERS INBOUND                 │
                        │   • REST Controllers (AuthController...)       │
                        │   • Middlewares (GlobalExceptionHandler)       │
                        │   • Configs (SecurityConfig, WebConfig)        │
                        └───────────────────────┬────────────────────────┘
                                                │ Invoca
                                                ▼
                        ┌────────────────────────────────────────────────┐
                        │               APPLICATION LAYER                │
                        │   • Services (AuthService, UserService...)     │
                        │   • Ports In & Out (UserRepository, etc.)      │
                        └───────┬────────────────────────────────┬───────┘
                                │ Usa                            │ Implementa
                                ▼                                ▼
       ┌─────────────────────────────────┐      ┌────────────────────────────────┐
       │          DOMAIN LAYER           │      │       ADAPTERS OUTBOUND        │
       │ • Models (User, Event, Ticket)  │      │ • Repositories (Spring Data)   │
       │ • Regras de Negócio Puras       │      │ • Adapters (UserRepoAdapter)   │
       │ • DTOs de Entrada e Saída       │      │ • Entities (UserEntity, etc.)  │
       │ • Enums e Objetos de Valor      │      │ • Security (BCrypt, JWT)       │
       └─────────────────────────────────┘      └────────────────────────────────┘
```

### Estrutura de Diretórios
```text
src/main/java/com/tickevent/app/
├── adapters/
│   ├── inbound/
│   │   ├── config/            # Configurações Spring Security e Web
│   │   ├── controllers/       # Adaptadores de entrada HTTP REST
│   │   └── middlewares/       # GlobalExceptionHandler, Interceptors
│   └── outbound/
│       ├── entities/          # Entidades JPA (@Entity, tabela do banco)
│       ├── ports/             # Implementações das portas de saída
│       ├── repositories/      # Interfaces Spring Data JPA
│       └── security/          # Provedor JWT e Hasher BCrypt
├── application/
│   ├── ports/                 # Interfaces de saída (UserRepository, TokenProvider)
│   └── service/               # Orquestração de casos de uso (AuthService, UserService)
├── domain/
│   ├── dtos/                  # Records de entrada (Requests) e saída (Responses)
│   └── models/                # Entidades ricas de domínio (User, Event, Ticket)
└── utils/
    └── mappers/               # Interfaces MapStruct (UserMapper, EventMapper)
```

---

## 3. Rotas Atualmente Disponíveis (API Reference)

A versão atual da API expõe o módulo prioritário de **Autenticação e Gestão de Identidade (`identity`)**.

**Base URL:** `http://localhost:8080/api/v1/auth`

| Método | Endpoint | Permissão | Descrição |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/register/client` | Pública | Cadastra um novo cliente comprador de ingressos |
| `POST` | `/api/v1/auth/register/admin` | Pública | Cadastra um novo produtor de eventos (`isApproved = false`) |
| `POST` | `/api/v1/auth/login` | Pública | Autentica e-mail/senha e retorna o Bearer Token |

---

### 3.1. Cadastro de Cliente

Registra um usuário final com perfil `USER`. A senha é automaticamente criptografada com hash BCrypt antes de ser persistida.

* **Endpoint:** `POST /api/v1/auth/register/client`
* **Headers:** `Content-Type: application/json`
* **Corpo da Requisição (Request Body):**
  ```json
  {
    "name": "Maria Silva",
    "email": "maria@tickevent.com",
    "password": "SenhaSegura123@",
    "phoneNumber": "11988887777",
    "document": "12345678901",
    "birthDate": "1995-05-10"
  }
  ```
* **Resposta de Sucesso (`HTTP 201 Created`):**
  ```json
  {
    "id": "7eb7d127-a61b-4ea3-bb3d-f611f8a8692a",
    "name": "Maria Silva",
    "email": "maria@tickevent.com",
    "phoneNumber": "11988887777",
    "role": "USER",
    "commercialName": null,
    "isApproved": null,
    "createdAt": "2026-09-03T12:33:58.353547798"
  }
  ```

---

### 3.2. Cadastro de Administrador / Produtor

Registra um organizador de eventos com perfil `ADMIN`. Por padrão da regra de domínio, o produtor inicia com `isApproved = false`, aguardando aprovação para publicação de eventos.

* **Endpoint:** `POST /api/v1/auth/register/admin`
* **Headers:** `Content-Type: application/json`
* **Corpo da Requisição (Request Body):**
  ```json
  {
    "name": "Carlos Produtor",
    "email": "carlos@producoes.com",
    "password": "SenhaAdmin123@",
    "phoneNumber": "11977776666",
    "document": "12345678000199",
    "commercialName": "Carlos Entretenimento Ltda",
    "bankAccountDetails": "Banco: 001, Ag: 1234, CC: 56789-0"
  }
  ```
* **Resposta de Sucesso (`HTTP 201 Created`):**
  ```json
  {
    "id": "97b60d82-7b83-442c-b7d0-fbeae8fbeedc",
    "name": "Carlos Produtor",
    "email": "carlos@producoes.com",
    "phoneNumber": "11977776666",
    "role": "ADMIN",
    "commercialName": "Carlos Entretenimento Ltda",
    "isApproved": false,
    "createdAt": "2026-09-03T12:34:39.903463631"
  }
  ```

---

### 3.3. Login e Autenticação

Valida as credenciais informadas e emite o token JWT de acesso.

* **Endpoint:** `POST /api/v1/auth/login`
* **Headers:** `Content-Type: application/json`
* **Corpo da Requisição (Request Body):**
  ```json
  {
    "email": "maria@tickevent.com",
    "password": "SenhaSegura123@"
  }
  ```
* **Resposta de Sucesso (`HTTP 200 OK`):**
  ```json
  {
    "token": "mock-jwt-token-for-7eb7d127-a61b-4ea3-bb3d-f611f8a8692a",
    "type": "Bearer"
  }
  ```

---

### 3.4. Tratamento Global de Erros

A aplicação conta com um middleware [`GlobalExceptionHandler`](file:///home/augusto-pedro/Documents/app/src/main/java/com/tickevent/app/adapters/inbound/middlewares/GlobalExceptionHandler.java) que padroniza todas as respostas de erro através do [`ErrorResponseDTO`](file:///home/augusto-pedro/Documents/app/src/main/java/com/tickevent/app/domain/dtos/middleware/ErrorResponseDTO.java):

#### Exemplo 1: E-mail Já Cadastrado (`HTTP 409 Conflict`)
Disparado ao tentar cadastrar um usuário com e-mail existente:
```json
{
  "message": "Email already exists",
  "httpStatus": 409,
  "timestamp": "2026-09-03T12:34:12.799189684"
}
```

#### Exemplo 2: Credenciais Inválidas (`HTTP 401 Unauthorized`)
Disparado ao informar senha incorreta ou e-mail inexistente no login:
```json
{
  "message": "Invalid credentials",
  "httpStatus": 401,
  "timestamp": "2026-09-03T12:34:54.068209684"
}
```

---

## 4. Como Rodar 100% com Docker

A aplicação e o banco de dados PostgreSQL estão totalmente dockerizados através do [`docker-compose.yml`](file:///home/augusto-pedro/Documents/app/docker-compose.yml) e do [`Dockerfile`](file:///home/augusto-pedro/Documents/app/Dockerfile) multi-stage build.

### Pré-requisitos
* **Docker** e **Docker Compose** instalados na máquina.

> [!IMPORTANT]
> **Aviso sobre a porta 5432:**
> Se você já tiver um serviço PostgreSQL rodando nativamente no seu sistema operacional (ex: serviço `postgresql` do Linux), a porta 5432 estará ocupada. 
> Para liberar a porta antes de iniciar o Docker:
> ```bash
> sudo systemctl stop postgresql # ou postgresql-18
> ```

---

### Subindo os Containers

Na raiz do projeto, execute:

```bash
# 1. Constrói a imagem da API e inicia os serviços em background
docker compose up --build -d
```

Para acompanhar os logs de inicialização da API:
```bash
docker compose logs -f api
```
*(Assim que aparecer a mensagem `Started AppApplication in ... seconds`, sua API estará pronta para receber requisições na porta `8080`)*.

---

### Testando via Terminal (cURL)

Abra outro terminal e execute os comandos abaixo para validar o fluxo completo:

#### 1. Cadastre um Cliente Comprador:
```bash
curl -i -X POST http://localhost:8080/api/v1/auth/register/client \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Maria Silva",
    "email": "maria@tickevent.com",
    "password": "SenhaSegura123@",
    "phoneNumber": "11988887777",
    "document": "12345678901",
    "birthDate": "1995-05-10"
  }'
```

#### 2. Teste a Validação de E-mail Duplicado:
Repita a requisição acima e veja o middleware retornar `409 Conflict`:
```bash
curl -i -X POST http://localhost:8080/api/v1/auth/register/client \
  -H "Content-Type: application/json" \
  -d '{"name": "Maria Silva", "email": "maria@tickevent.com", ...}'
```

#### 3. Cadastre um Produtor de Eventos:
```bash
curl -i -X POST http://localhost:8080/api/v1/auth/register/admin \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Carlos Produtor",
    "email": "carlos@producoes.com",
    "password": "SenhaAdmin123@",
    "phoneNumber": "11977776666",
    "document": "12345678000199",
    "commercialName": "Carlos Entretenimento Ltda",
    "bankAccountDetails": "Banco: 001, Ag: 1234, CC: 56789-0"
  }'
```

#### 4. Realize o Login com Sucesso:
```bash
curl -i -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria@tickevent.com",
    "password": "SenhaSegura123@"
  }'
```

---

### Inspecionando o Banco de Dados no Container

Você pode verificar os dados gravados na tabela `users` dentro do container PostgreSQL:

```bash
docker exec -it tickevent-postgres psql -U postgres -d tickevent_db -c "SELECT id, name, email, role, is_approved, created_at FROM users;"
```

---

### Troubleshooting & Dicas do Docker

| Ação | Comando |
| :--- | :--- |
| **Ver status dos containers** | `docker compose ps` |
| **Acompanhar logs em tempo real** | `docker compose logs -f` |
| **Parar todos os containers** | `docker compose down` |
| **Parar e apagar os volumes de dados** | `docker compose down -v` |
| **Reconstruir apenas a API após alterações** | `docker compose up --build -d api` |

---

## 5. Execução em Modo de Desenvolvimento Local

Se preferir rodar apenas o banco de dados no Docker e a aplicação diretamente na sua IDE ou via Maven:

1. Inicie apenas o container do banco:
   ```bash
   docker compose up -d db
   ```
2. Execute a aplicação Spring Boot com o wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 6. Testes Automatizados

O projeto conta com suíte de testes unitários e de integração cobrindo serviços, repositórios com banco em memória e controladores HTTP:

```bash
# Executa todos os testes da aplicação
./mvnw test
```

Para rodar apenas os testes dos controladores:
```bash
./mvnw test -Dtest=AuthControllerTest
```
