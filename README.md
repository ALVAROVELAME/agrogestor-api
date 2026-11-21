# 🐄 AgroGestor API

### Backend REST para gestão de rebanho leiteiro

API REST desenvolvida com Spring Boot para gerenciamento de rebanho leiteiro, com autenticação JWT, confirmação de e-mail e isolamento de dados por usuário.

[Spring Boot](https://spring.io/projects/spring-boot) ([image](https://img.shields.io/badge/Spring%20Boot-4.0.9-6DB33F?logo=springboot&logoColor=white))
[Java](https://openjdk.org/) ([image](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white))
[MariaDB](https://mariadb.org/) ([image](https://img.shields.io/badge/MariaDB-11-003545?logo=mariadb&logoColor=white))
[JWT](https://github.com/jwtk/jjwt) ([image](https://img.shields.io/badge/JWT-jjwt--0.13.0-000000?logo=jsonwebtokens&logoColor=white))
[Docker](https://www.docker.com/) ([image](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white))

[🌐 API em produção](https://agrogestor-api.duckdns.org/) · [📘 Swagger UI](https://agrogestor-api.duckdns.org/swagger-ui.html)

---

## 📖 Sobre o projeto

O **AgroGestor API** é o backend do sistema AgroGestor, uma aplicação voltada à gestão de rebanho leiteiro.

A API oferece autenticação, contas, recuperação de senha, gestão de animais, produção, tarefas, eventos, lançamentos financeiros e relatórios, com arquitetura REST em camadas e isolamento de dados por usuário.

Além da aplicação, o projeto possui uma infraestrutura de produção baseada em **máquinas virtuais, Docker, Nginx, banco de dados separado, HTTPS com renovação automatizada de certificados e pipeline de deploy automatizado**.

### Diferenciais

* 🔐 **JWT stateless** com filtro de autenticação customizado
* 📧 **Confirmação de e-mail** com token expirável
* 🛡️ **Isolamento de dados por usuário**
* 🗄️ **JPA + Hibernate** para persistência
* 📚 **Swagger/OpenAPI** para documentação interativa
* 🐳 **Docker** para containerização
* ☁️ **Infraestrutura virtualizada** com separação entre aplicação e dados
* 🌐 **Nginx como reverse proxy**
* 🔒 **HTTPS com certificados Let's Encrypt**
* ♻️ **Renovação automática de certificados através do Certbot**
* 🚀 **Deploy automatizado** com GitHub Actions
* ✅ **Bean Validation** para validação de entrada
* 🎯 **Tratamento global de exceções**
* 🔑 **Segredos mantidos fora do código-fonte**

---

## ✨ Funcionalidades

### Autenticação e conta

* ✅ Cadastro com validação de nome, e-mail e senha
* ✅ Confirmação de e-mail por token único
* ✅ Expiração do token de confirmação
* ✅ Login com JWT
* ✅ Endpoint `/me` para consultar o usuário autenticado
* ✅ Exclusão de conta mediante confirmação por senha
* ✅ Senhas armazenadas com **BCrypt**
* ✅ Recuperação de senha por e-mail com token de uso único

### Rebanho

* ✅ Listagem de animais do usuário autenticado
* ✅ Criação de animais com validação
* ✅ Atualização por ID
* ✅ Exclusão por ID
* ✅ Isolamento de dados entre usuários
* ✅ Ficha do animal com dados complementares

### Produção e gestão

* Registro de produção por animal, data e turno
* Tarefas, eventos e lançamentos financeiros
* Relatórios e indicadores, com exportação CSV

---

# 🛠️ Stack técnica

## Core

| Tecnologia                                                                          | Versão | Uso                 |
| ----------------------------------------------------------------------------------- | -----: | ------------------- |
| [Spring Boot](https://spring.io/projects/spring-boot)                               |  4.0.9 | Framework principal |
| [Java](https://openjdk.org/)                                                        |     17 | Linguagem           |
| [Spring Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html) |      — | Camada REST         |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa)                       |      — | Persistência        |
| [Hibernate](https://hibernate.org/)                                                 |    7.x | ORM                 |
| [MariaDB Connector/J](https://mariadb.com/kb/en/about-mariadb-connector-j/)         |      — | Driver JDBC         |

## Segurança e autenticação

| Tecnologia                                                    | Versão | Uso                        |
| ------------------------------------------------------------- | -----: | -------------------------- |
| [Spring Security](https://spring.io/projects/spring-security) |      — | Segurança e filtros        |
| [JJWT](https://github.com/jwtk/jjwt)                          | 0.13.0 | Geração e validação de JWT |
| [BCrypt](https://en.wikipedia.org/wiki/Bcrypt)                |      — | Hash de senhas             |

## Comunicação e documentação

| Tecnologia                                                                              | Versão | Uso                  |
| --------------------------------------------------------------------------------------- | -----: | -------------------- |
| [Spring Mail](https://docs.spring.io/spring-framework/reference/integration/email.html) |      — | Envio de e-mails     |
| [SpringDoc OpenAPI](https://springdoc.org/)                                             |  3.0.3 | Swagger UI / OpenAPI |
| [Bean Validation](https://beanvalidation.org/)                                          |      — | Validação de DTOs    |

## DevOps e infraestrutura

| Tecnologia                                            | Uso                                           |
| ----------------------------------------------------- | --------------------------------------------- |
| [Maven Wrapper](https://maven.apache.org/wrapper/)    | Build reproduzível                            |
| [Docker](https://www.docker.com/)                     | Containerização                               |
| [Docker Compose](https://docs.docker.com/compose/)    | Orquestração                                  |
| [Nginx](https://nginx.org/)                           | Reverse proxy                                 |
| [Certbot](https://certbot.eff.org/)                   | Gerenciamento e renovação de certificados TLS |
| Let's Encrypt                                         | Emissão de certificados HTTPS                 |
| [GitHub Actions](https://github.com/features/actions) | CI/CD                                         |
| Self-hosted Runner                                    | Execução automatizada do deploy               |
| KVM                                                   | Virtualização das máquinas                    |

---

# ☁️ Arquitetura de produção

A infraestrutura de produção foi organizada em duas camadas principais:

```text
                         INTERNET
                             │
                             ▼
                  ┌─────────────────────┐
                  │       Nginx         │
                  │   Reverse Proxy     │
                  │     HTTP / HTTPS    │
                  └──────────┬──────────┘
                             │
                             ▼
                  ┌─────────────────────┐
                  │    VM DE APIs       │
                  │                     │
                  │      Docker         │
                  │                     │
                  │  ┌───────────────┐  │
                  │  │ AgroGestor    │  │
                  │  │ API           │  │
                  │  └───────────────┘  │
                  │                     │
                  │  Outros serviços    │
                  │  independentes      │
                  └──────────┬──────────┘
                             │
                      Rede privada
                             │
                             ▼
                  ┌─────────────────────┐
                  │    VM DE DADOS      │
                  │                     │
                  │      Docker         │
                  │                     │
                  │  ┌───────────────┐  │
                  │  │   MariaDB     │  │
                  │  └───────────────┘  │
                  │                     │
                  │  ┌───────────────┐  │
                  │  │   MongoDB     │  │
                  │  └───────────────┘  │
                  └─────────────────────┘
```

### Separação de responsabilidades

A infraestrutura separa a camada de aplicação da camada de persistência:

```text
Aplicação
   │
   ├── API REST
   ├── Autenticação
   ├── Regras de negócio
   └── Documentação
          │
          ▼
      Persistência
          │
          ├── MariaDB
          └── MongoDB
```

Essa organização reduz o acoplamento entre os serviços e permite administrar aplicação e dados como camadas independentes.

---

# 🌐 Nginx como reverse proxy

O Nginx funciona como ponto central de entrada para os serviços HTTP.

Fluxo simplificado:

```text
Cliente
   │
   ▼
HTTPS
   │
   ▼
Nginx
   │
   ├──► AgroGestor API
   │
   ├──► Outros serviços
   │
   └──► Outros endpoints internos
```

As aplicações permanecem desacopladas da camada pública de entrada, enquanto o Nginx centraliza o encaminhamento das requisições.

Isso também facilita a utilização de HTTPS, organização dos endpoints e futura expansão da infraestrutura.

---

# 🔒 HTTPS e renovação automática de certificados

O ambiente de produção utiliza **HTTPS** através do Nginx.

Os certificados TLS são obtidos através do **Let's Encrypt** e gerenciados pelo **Certbot**.

A renovação automática é executada pelo sistema operacional através de um agendamento baseado em **systemd timer**.

Fluxo simplificado:

```text
Let's Encrypt
      │
      ▼
   Certbot
      │
      ▼
Systemd Timer
      │
      ▼
Renovação automática
      │
      ▼
    Nginx
      │
      ▼
    HTTPS
```

A documentação pública não inclui:

* credenciais;
* chaves privadas;
* certificados privados;
* endereços internos;
* identificadores de máquinas;
* configurações internas de acesso.

---

# 🗄️ Camada de dados

A persistência é mantida em uma camada separada da aplicação.

### Bancos utilizados

| Banco       | Uso                                               |
| ----------- | ------------------------------------------------- |
| **MariaDB** | Persistência relacional do AgroGestor             |
| **MongoDB** | Persistência de outros serviços da infraestrutura |

O **AgroGestor API utiliza MariaDB** como banco relacional principal.

A separação entre aplicação e banco permite evoluir os serviços de forma independente e facilita a organização operacional do ambiente.

---

# 🔐 Comunicação entre as camadas

A comunicação entre aplicação e persistência ocorre através da **rede privada da infraestrutura**.

Fluxo lógico:

```text
VM DE APIs
     │
     │ Rede privada
     ▼
VM DE DADOS
     │
     ├──► MariaDB
     │
     └──► MongoDB
```

Detalhes de endereçamento e acesso interno não fazem parte da documentação pública.

---

# 🧠 Gerenciamento de recursos

Os serviços são executados em containers com **limites de recursos definidos individualmente**.

Esse modelo permite:

* evitar que um único serviço consuma todos os recursos disponíveis;
* reduzir impacto de picos de utilização;
* controlar melhor o comportamento dos containers;
* facilitar a operação de múltiplas aplicações no mesmo ambiente.

Exemplo conceitual:

```text
VM
│
├── API A
│   └── limite de recursos
│
├── API B
│   └── limite de recursos
│
└── API C
    └── limite de recursos
```

Os valores operacionais específicos não são publicados no README.

---

# 📌 Princípios utilizados na infraestrutura

A arquitetura foi estruturada seguindo alguns princípios:

* **Separação de responsabilidades**
* **Containerização dos serviços**
* **Reverse proxy centralizado**
* **HTTPS**
* **Renovação automatizada de certificados TLS**
* **Limitação de recursos por container**
* **Rede privada entre camadas internas**
* **Automação de deploy**
* **Ambiente reproduzível com Docker**
* **Separação entre aplicação e persistência**
* **Serviços independentes**

---

# 📁 Estrutura do projeto

```text
agrogestor-api/
├── .github/                 # workflows de testes e deploy, e scripts de verificação
├── .mvn/wrapper/            # Maven Wrapper
├── docs/                    # revisão técnica e detalhes das funcionalidades
├── scripts/                 # verificação de dependências
├── src/
│   ├── main/java/com/agrogestor/api/
│   │   ├── config/          # segurança, CORS e propriedades JWT
│   │   ├── controller/      # endpoints REST; conta/ reúne os de conta
│   │   ├── dto/             # entrada e saída; animal/, auth/ e usuario/
│   │   ├── exception/       # exceções e tratamento global
│   │   ├── model/           # entidades; conta/ reúne usuário e cadastro
│   │   ├── repository/      # persistência; conta/ reúne repositórios de conta
│   │   ├── security/        # filtro JWT
│   │   ├── service/         # regras de negócio; auth/ e pecuaria/
│   │   └── validation/      # validação de senhas BCrypt
│   ├── main/resources/      # configuração da aplicação
│   └── test/                # testes unitários, HTTP e de integração
├── logs/                    # logs locais, quando existentes (ignorados pelo Git)
├── .env.example             # exemplo de configuração
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── mvnw
└── mvnw.cmd
```

---

# 🌐 Endpoints da API

## Base URL

| Ambiente     | URL                                  |
| ------------ | ------------------------------------ |
| **Produção** | `https://agrogestor-api.duckdns.org` |
| **Local (Maven)** | `http://localhost:8080` |
| **Local (Compose)** | `http://localhost:3002` |

## 🔓 Públicos

| Método | Endpoint                        | Descrição             |
| ------ | ------------------------------- | --------------------- |
| `GET`  | `/`                             | Health check          |
| `POST` | `/api/auth/login`               | Autenticação          |
| `GET`  | `/api/auth/confirmar?token=...` | Confirmação de e-mail |
| `POST` | `/api/usuarios`                 | Inicia cadastro       |
| `POST` | `/api/auth/forgot-password` | Solicita recuperação de senha |
| `POST` | `/api/auth/reset-password` | Redefine senha com token |

## 🔐 Protegidos

Requerem:

```http
Authorization: Bearer <token>
```

| Método   | Endpoint            | Descrição                     |
| -------- | ------------------- | ----------------------------- |
| `GET`    | `/api/auth/me`      | Retorna o usuário autenticado |
| `DELETE` | `/api/usuarios/me`  | Exclui a conta                |
| `GET`    | `/api/animais`      | Lista animais                 |
| `POST`   | `/api/animais`      | Cria animal                   |
| `PUT`    | `/api/animais/{id}` | Atualiza animal               |
| `DELETE` | `/api/animais/{id}` | Exclui animal                 |
| `GET` | `/api/animais/{id}` | Consulta animal |
| `PATCH` | `/api/animais/{id}/ficha` | Atualiza dados complementares |
| `GET`, `POST` | `/api/producoes` | Lista e registra produção |
| `PUT`, `DELETE` | `/api/producoes/{id}` | Atualiza ou exclui produção |
| `GET`, `POST` | `/api/tarefas` | Lista e cria tarefas |
| `PATCH`, `DELETE` | `/api/tarefas/{id}` | Atualiza ou exclui tarefa |
| `GET`, `POST` | `/api/eventos` | Lista e cria eventos |
| `PUT`, `DELETE` | `/api/eventos/{id}` | Atualiza ou exclui evento |
| `GET`, `POST` | `/api/financeiro/lancamentos` | Lista e cria lançamentos |
| `PUT`, `DELETE` | `/api/financeiro/lancamentos/{id}` | Atualiza ou exclui lançamento |
| `GET` | `/api/relatorios/producao`, `/api/relatorios/financeiro`, `/api/relatorios/eventos` | Consulta relatórios |
| `GET` | `/api/indicadores` | Consulta indicadores |
| `GET` | `/api/relatorios/exportacao` | Exporta relatório agregado em CSV |

As listagens de produção, tarefas, eventos e lançamentos usam `pagina` (inicia em 0) e `tamanho` (1 a 100; padrão 20). Os relatórios exigem `inicio` e `fim` no formato `AAAA-MM-DD`, com intervalo de até 366 dias; indicadores também exigem `referencia`. A exportação usa `tipo=PRODUCAO`, `FINANCEIRO` ou `EVENTOS`. Filtros, exemplos e regras estão em [docs/novas-funcionalidades-api.md](docs/novas-funcionalidades-api.md).

## ⚠️ Respostas de erro

| Status | Situação |
| --- | --- |
| `400` | Dados inválidos, JSON malformado ou token de confirmação inválido/expirado |
| `401` | Credenciais inválidas, ausência de JWT ou token que não corresponde a uma conta ativa |
| `403` | Conta ainda não confirmada no login, após validação da senha, ou origem CORS não permitida |
| `404` | Animal inexistente ou pertencente a outro usuário |
| `409` | E-mail já cadastrado, confirmação ainda pendente ou conflito de integridade |
| `429` | Limite de tentativas de recuperação de senha atingido |
| `500` | Falha interna, sem detalhes sensíveis na resposta |

## 📘 Documentação interativa

### Swagger UI

```text
https://agrogestor-api.duckdns.org/swagger-ui.html
```

### OpenAPI

```text
https://agrogestor-api.duckdns.org/v3/api-docs
```

---

# 📡 Exemplos de requisição

## 1. Cadastro

```bash
curl -X POST https://agrogestor-api.duckdns.org/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João da Silva",
    "email": "joao@example.com",
    "senha": "senhaSegura123"
  }'
```

### Resposta

```json
{
  "sucesso": true,
  "mensagem": "Cadastro iniciado. Verifique seu email para confirmar a conta."
}
```

---

## 2. Login

```bash
curl -X POST https://agrogestor-api.duckdns.org/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "senha": "senhaSegura123"
  }'
```

### Resposta

```json
{
  "sucesso": true,
  "mensagem": "Login realizado com sucesso",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "usuario": {
    "id": 1,
    "nome": "João da Silva",
    "email": "joao@example.com",
    "ativo": true
  }
}
```

---

## 3. Criar animal

```bash
curl -X POST https://agrogestor-api.duckdns.org/api/animais \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "brinco": "A-1024",
    "nome": "Mimosa",
    "categoria": "VACA_EM_LACTACAO",
    "producaoDiaria": 24.5
  }'
```

### Categorias

```text
BEZERRA
NOVILHA
VACA_EM_LACTACAO
VACA_SECA
```

---

## 4. Excluir conta

```bash
curl -X DELETE https://agrogestor-api.duckdns.org/api/usuarios/me \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "senha": "senhaSegura123"
  }'
```

---

# 🗄️ Modelo de dados

## `usuarios`

| Coluna                | Tipo     | Restrições         |
| --------------------- | -------- | ------------------ |
| `id`                  | BIGINT   | PK, auto-increment |
| `nome`                | VARCHAR  | NOT NULL           |
| `email`               | VARCHAR  | NOT NULL, UNIQUE   |
| `senha_hash`          | VARCHAR  | NOT NULL, BCrypt   |
| `ativo`               | BOOLEAN  | NOT NULL           |
| `token_confirmacao`   | VARCHAR  | UNIQUE, nullable   |
| `token_expira_em`     | DATETIME | nullable           |
| `email_confirmado_em` | DATETIME | nullable           |
| `criado_em`           | DATETIME | NOT NULL           |
| `recuperacao_token_hash`, `recuperacao_expira_em`, `recuperacao_solicitada_em` | VARCHAR/DATETIME | Recuperação de senha |
| `versao_sessao` | BIGINT | Versão usada para invalidar sessões |

## `animais`

| Coluna            | Tipo     | Restrições                   |
| ----------------- | -------- | ---------------------------- |
| `id`              | BIGINT   | PK, auto-increment           |
| `usuario_id`      | BIGINT   | FK → `usuarios.id`, NOT NULL |
| `brinco`          | VARCHAR  | NOT NULL                     |
| `nome`            | VARCHAR  | NOT NULL                     |
| `categoria`       | VARCHAR  | ENUM                         |
| `producao_diaria` | DOUBLE   | NOT NULL                     |
| `criado_em`       | DATETIME | NOT NULL                     |
| `atualizado_em`   | DATETIME | NOT NULL                     |
| `data_nascimento`, `raca`, `origem`, `observacoes` | DATE/VARCHAR | Dados opcionais da ficha |

## `cadastros_pendentes`

| Coluna              | Tipo     | Restrições         |
| ------------------- | -------- | ------------------ |
| `id`                | BIGINT   | PK, auto-increment |
| `nome`              | VARCHAR  | NOT NULL           |
| `email`             | VARCHAR  | NOT NULL, UNIQUE   |
| `senha_hash`        | VARCHAR  | NOT NULL           |
| `token_confirmacao` | VARCHAR  | NOT NULL, UNIQUE   |
| `token_expira_em`   | DATETIME | NOT NULL           |
| `criado_em`         | DATETIME | NOT NULL           |

## `producoes` e `registros_gestao`

`producoes` associa usuário e animal a data, turno e litros, com unicidade por animal, data e turno. `registros_gestao` armazena tarefas, eventos e lançamentos financeiros, vinculados ao usuário e, quando aplicável, a um animal. A exclusão do animal remove seus registros associados.

---

# 🔐 Fluxo de autenticação

```text
┌───────────────────────────────────────────────────────────┐
│ 1. CADASTRO                                               │
├───────────────────────────────────────────────────────────┤
│ POST /api/usuarios                                        │
│       ↓                                                   │
│ UsuarioService.cadastrar()                                │
│       ↓                                                   │
│ Cria CadastroPendente                                     │
│       ↓                                                   │
│ Gera token de confirmação                                  │
│       ↓                                                   │
│ EmailService envia link                                   │
└───────────────────────────────────────────────────────────┘
                           │
                           ▼
┌───────────────────────────────────────────────────────────┐
│ 2. CONFIRMAÇÃO                                            │
├───────────────────────────────────────────────────────────┤
│ GET /api/auth/confirmar?token=<uuid>                      │
│       ↓                                                   │
│ EmailConfirmationService.confirmarEmail()                 │
│       ↓                                                   │
│ Valida token e expiração                                  │
│       ↓                                                   │
│ Cria usuário ativo                                        │
│       ↓                                                   │
│ Remove cadastro pendente                                  │
└───────────────────────────────────────────────────────────┘
                           │
                           ▼
┌───────────────────────────────────────────────────────────┐
│ 3. LOGIN                                                  │
├───────────────────────────────────────────────────────────┤
│ POST /api/auth/login                                      │
│       ↓                                                   │
│ AuthService.autenticar()                                  │
│       ↓                                                   │
│ BCrypt + credenciais                                      │
│       ↓                                                   │
│ JwtService.gerarToken()                                   │
│       ↓                                                   │
│ Retorna { token, usuario }                               │
└───────────────────────────────────────────────────────────┘
                           │
                           ▼
┌───────────────────────────────────────────────────────────┐
│ 4. REQUISIÇÕES AUTENTICADAS                               │
├───────────────────────────────────────────────────────────┤
│ Authorization: Bearer <token>                             │
│       ↓                                                   │
│ JwtAuthenticationFilter                                   │
│       ↓                                                   │
│ Valida assinatura e identidade                            │
│       ↓                                                   │
│ Carrega Usuario                                            │
│       ↓                                                   │
│ SecurityContext                                            │
│       ↓                                                   │
│ Controller                                                 │
└───────────────────────────────────────────────────────────┘
```


## Recuperação de senha

Os endpoints públicos `POST /api/auth/forgot-password` (JSON `{"email":"..."}`) e
`POST /api/auth/reset-password` (JSON `{"token":"...","senha":"..."}`) permitem
recuperar o acesso. A resposta de solicitação é genérica; o link enviado por e-mail
expira em 30 minutos e só pode ser usado uma vez. A redefinição invalida os JWTs
anteriores. O envio usa as configurações SMTP e `APP_FRONTEND_URL` existentes.

O Hibernate está configurado com `ddl-auto: update`: a implantação exige permissão
para adicionar colunas à tabela de usuários. Em produção, planeje e valide a
migração de esquema antes de publicar. O limite de tentativas atual é local a cada
instância e utiliza o endereço visto pela aplicação; em múltiplas instâncias ou
atrás de proxy, configure também limitação compartilhada e cabeçalhos de origem
confiáveis no provedor de infraestrutura.

---

# 🏗️ Arquitetura em camadas

```text
┌────────────────────────────────────────────────────────────┐
│                    Controller (REST)                       │
│  Recebe HTTP, valida entrada e delega para Services        │
└──────────────────────────────┬─────────────────────────────┘
                               │
┌──────────────────────────────▼─────────────────────────────┐
│                      Service Layer                         │
│  Regras de negócio, orquestração e transações              │
└──────────────────────────────┬─────────────────────────────┘
                               │
┌──────────────────────────────▼─────────────────────────────┐
│                       Repository                            │
│  Persistência e consultas através do Spring Data JPA        │
└──────────────────────────────┬─────────────────────────────┘
                               │
┌──────────────────────────────▼─────────────────────────────┐
│                        Model / JPA                          │
│  Entidades e relacionamentos                                │
└────────────────────────────────────────────────────────────┘
```

## Padrões utilizados

| Padrão                       | Aplicação                         |
| ---------------------------- | --------------------------------- |
| **DTO**                      | Separação entre API e entidades   |
| **Repository**               | Spring Data JPA                   |
| **Service Layer**            | Regras de negócio                 |
| **Dependency Injection**     | Injeção por construtor            |
| **Global Exception Handler** | `@RestControllerAdvice`           |
| **Stateless Authentication** | JWT + `STATELESS`                 |
| **Layered Architecture**     | Controller → Service → Repository |

---

# 🚀 Como executar localmente

## Pré-requisitos

* **Java 17**
* **MariaDB acessível e configuração SMTP**
* **Docker e Docker Compose** para execução em contêiner ou testes de integração
* **Git**

## Opção 1 — Docker Compose

### 1. Clone

```bash
git clone https://github.com/ALVAROVELAME/agrogestor-api.git
cd agrogestor-api
```

### 2. Configure o ambiente

Crie um arquivo `.env` local a partir do modelo disponibilizado no projeto:

```bash
cp .env.example .env
```

Edite os valores de acordo com seu ambiente local. O Compose deste repositório publica a API em `127.0.0.1:3002` e requer um MariaDB externo acessível pelo container. No Docker Desktop, `host.docker.internal` permite acessar um banco executado no host. O banco não é criado pelo Compose.

A porta interna do Compose é `8080`; mantenha `PORT=8080` ao usar o mapeamento padrão. Para execução direta no host, ajuste `DB_HOST` para o endereço acessível pelo host.

### 3. Suba a aplicação

```bash
docker compose up -d --build
```

### 4. Consulte os logs

```bash
docker compose logs -f agrogestor_api
```

### 5. Teste

```bash
curl http://localhost:3002/
```

Resposta esperada:

```text
API AgroGestor funcionando
```

---

## Opção 2 — Maven

```bash
./mvnw clean verify
java -jar target/agrogestor-api-0.0.1-SNAPSHOT.jar
```

Essa opção requer Docker ativo durante `verify` e um MariaDB acessível para executar a aplicação. Exporte as variáveis no terminal antes de iniciar o JAR: o Spring Boot não carrega `.env` automaticamente. No Windows, use `.\mvnw.cmd`. Para somente empacotar com testes unitários/HTTP, use `./mvnw clean package`.

---

# ⚙️ Variáveis de ambiente

Exemplo de configuração local:

```env
# ===== BANCO DE DADOS =====
DB_HOST=host.docker.internal
DB_PORT=3306
DB_NAME=agrogestor
DB_USER=agrogestor
DB_PASSWORD=defina_uma_senha_local

# ===== JWT =====
JWT_SECRET=substitua_por_uma_chave_aleatoria_de_pelo_menos_32_bytes
JWT_EXPIRATION=86400000
JWT_ISSUER=agrogestor-api

# ===== EMAIL =====
MAIL_HOST=smtp.exemplo.com
MAIL_PORT=587
MAIL_USERNAME=seu_email@exemplo.com
MAIL_PASSWORD=sua_credencial_local
MAIL_FROM=AgroGestor <seu_email@exemplo.com>

# ===== APLICAÇÃO =====
APP_FRONTEND_URL=http://localhost:5173

# ===== PORTA =====
PORT=8080
```

### ⚠️ Segurança

Nunca publique:

* arquivos `.env`;
* chaves JWT;
* senhas de banco;
* credenciais SMTP;
* tokens;
* chaves privadas;
* certificados privados;
* secrets de CI/CD.

Para ambientes de produção, utilize mecanismos apropriados de gerenciamento de segredos e variáveis protegidas do pipeline.

---

# 🐳 Docker

## Dockerfile

O projeto utiliza uma estratégia **multi-stage build**, separando a etapa de compilação da imagem final de execução.

```dockerfile
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY . .

RUN sh ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

USER 10001:10001

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
```

## Docker Compose do projeto

```yaml
services:
  agrogestor_api:
    build: .
    env_file:
      - .env

    ports:
      - "127.0.0.1:3002:8080"

    restart: unless-stopped

    deploy:
      resources:
        limits:
          memory: 400M
          cpus: "0.50"
```

### Comandos úteis

```bash
# Subir
docker compose up -d --build

# Logs
docker compose logs -f agrogestor_api

# Parar
docker compose down

# Build sem cache
docker compose build --no-cache

# Consumo dos containers
docker stats --no-stream
```

---

# 🚢 Deploy automatizado

O deploy é realizado através de **GitHub Actions** com execução automatizada no ambiente de produção.

## Fluxo

```text
Push na branch main
        ↓
GitHub Actions
        ↓
Testes unitários, HTTP e MariaDB em Docker
        ↓
Runner de deploy
        ↓
Validação da configuração
        ↓
Atualização da aplicação
        ↓
Build da nova imagem
        ↓
Inicialização dos serviços
        ↓
Validação
        ↓
Limpeza de recursos antigos
```

A automação reduz a necessidade de intervenção manual e torna o processo de atualização mais previsível.

As credenciais e informações sensíveis utilizadas no processo de deploy permanecem fora do código-fonte.

---

# 🧪 Testes

O projeto utiliza **JUnit Jupiter**, **Mockito**, **MockMvc** e **Testcontainers com MariaDB real**, com cobertura gerada pelo **JaCoCo**.

> **Compatibilidade:** o Spring Boot 4 / Spring Framework 7 exige JUnit 6 para a integração com Spring Test. A suíte usa o Jupiter 6 gerenciado pelo Boot, mantendo a API `org.junit.jupiter` conhecida do JUnit 5. Fixar JUnit 5 neste projeto causa `NoSuchMethodError` no carregamento dos testes HTTP. Veja a [baseline oficial do Spring Framework 7](https://spring.io/blog/2025/11/13/spring-framework-7-0-general-availability/).

## Testes unitários e HTTP (sem Docker)

```bash
./mvnw test
```

No Windows/PowerShell:

```powershell
.\mvnw.cmd test
```

Os serviços usam Mockito para simular repositórios e SMTP. Os testes HTTP usam MockMvc com a cadeia real de segurança e tokens JWT assinados, verificando rotas, validação, autenticação, CORS e respostas de erro.

## Suíte completa, incluindo integração

```bash
./mvnw clean verify
```

```powershell
.\mvnw.cmd clean verify
```

Requer **Docker ativo**, Java 17 e acesso à internet no primeiro download. O Testcontainers inicia `mariadb:11.4` em uma porta aleatória, cria um banco descartável e remove o container ao final. Não usa banco, SMTP ou credenciais de produção. Somente o envio SMTP é simulado na integração.

O Maven Surefire executa `*Test`; o Failsafe executa `*IT` durante `verify`. Sem Docker, a integração **falha explicitamente**; ela não é ignorada silenciosamente.

### Cenários de integração

* Cadastro → confirmação → login → criação, atualização, listagem e exclusão de animal.
* Isolamento de dados entre usuários e resposta 404 para animal de outro dono.
* Exclusão de conta com senha e invalidação de JWT antigo, inclusive após recadastro.
* Rollback do cadastro quando o SMTP falha e renovação de cadastro expirado.
* Confirmação concorrente de e-mail consumindo o token apenas uma vez.
* Rollback da confirmação diante de falha no banco.
* Carregamento do OpenAPI e Swagger UI.

## Teste específico

```bash
./mvnw test -Dtest=AnimalServiceTest
./mvnw verify -Dit.test=AgrogestorApiIT
```

## Resultados e cobertura

Na execução local de 21/11/2026, `clean test jacoco:report` concluiu **99 testes unitários e HTTP**, com **89,9% de cobertura de linhas** e **73,8% de ramificações**. Os testes de integração não foram executados nessa medição porque o Docker estava indisponível. A cobertura é um retrato dessa execução; gere novamente o relatório para avaliar alterações posteriores.

Após `verify`, consulte:

```text
target/surefire-reports/
target/failsafe-reports/
target/site/jacoco/index.html
```

O GitHub Actions executa a suíte em pull requests e antes do deploy da `main`, publicando os resultados e a cobertura como artefatos. O deploy depende do sucesso dos testes e utiliza o commit testado.

## Verificação de dependências

Com Python 3 e acesso à internet:

```bash
./mvnw dependency:tree -DoutputType=json -DoutputFile=target/dependency-tree.json
python scripts/check_dependencies.py
```

No PowerShell, coloque os argumentos `-D...` entre aspas. O script consulta os pacotes resolvidos na [OSV](https://osv.dev/), incluindo dependências transitivas e de teste, e grava `target/security-audit.json`. Retorna código 1 se encontrar avisos e 2 se a consulta falhar. A ausência de avisos conhecidos não é uma garantia de ausência de vulnerabilidades.

A revisão de código, limpeza e decisões de compatibilidade estão em [docs/REVISAO.md](docs/REVISAO.md).

---

# 🔒 Segurança

## Implementado

* ✅ BCrypt para armazenamento de senhas
* ✅ JWT assinado com HMAC, emissor e expiração validados, vinculado ao ID da conta
* ✅ Autenticação stateless
* ✅ CORS limitado a origens explícitas (sem curingas para Vercel/DuckDNS)
* ✅ CSRF desabilitado para API stateless
* ✅ Isolamento de dados por usuário
* ✅ Validação de DTOs, incluindo limite BCrypt de 72 bytes UTF-8
* ✅ Confirmação de senha para exclusão de conta
* ✅ Expiração de tokens e bloqueio de contas inativas
* ✅ Erros internos sem detalhes sensíveis na resposta
* ✅ Cadastro e confirmação transacionais
* ✅ Container executado sem root e segredos excluídos do contexto de build
* ✅ HTTPS em produção
* ✅ Certificados TLS gerenciados pelo Certbot
* ✅ Renovação automática de certificados
* ✅ Segredos mantidos fora do código-fonte

### Isolamento multi-tenant

Cada animal pertence a um usuário através da associação:

```text
Usuario
   │
   └── usuario_id
          │
          ▼
       Animal
```

As operações autenticadas utilizam a identidade do usuário presente no `SecurityContext` para restringir o acesso aos dados correspondentes.

---

# 🛡️ Boas práticas para publicação

Este repositório pode ser utilizado como documentação técnica e portfólio sem expor informações operacionais do ambiente de produção.

Antes de publicar alterações, evite versionar:

```text
.env
.env.*
*.pem
*.key
*.p12
*.jks
```

Também é recomendado manter fora do repositório:

* credenciais de banco;
* credenciais SMTP;
* segredos JWT;
* tokens de acesso;
* chaves privadas;
* identificadores internos de infraestrutura;
* configurações específicas de acesso à produção.

O projeto deve utilizar arquivos de exemplo para demonstrar a configuração necessária sem revelar valores reais.

---

# 📊 Logs e monitoramento

## Logs do container

```bash
docker logs --tail 100 agrogestor_api
```

```bash
docker logs -f agrogestor_api
```

## Estatísticas

```bash
docker stats agrogestor_api
```

## Health check

```bash
curl -s https://agrogestor-api.duckdns.org/
```

Resposta esperada:

```text
API AgroGestor funcionando
```

---

# 🗺️ Roadmap

## Concluído ✅

* Cadastro com confirmação de e-mail
* Login com JWT
* CRUD de animais
* Isolamento de dados por usuário
* Exclusão de conta
* Swagger/OpenAPI
* Dockerização
* Infraestrutura virtualizada
* Separação entre aplicação e banco de dados
* Nginx como reverse proxy
* HTTPS em produção
* Renovação automática de certificados TLS
* Deploy automatizado com GitHub Actions
* Tratamento global de exceções
* Recuperação de senha, produção, gestão e relatórios
* Testes de integração com Testcontainers

## Em planejamento 📋

* Refresh token
* Limitação de requisições compartilhada entre instâncias
* Logs estruturados em JSON
* Métricas com Prometheus/Grafana
* Migrations com Flyway
* Multi-fazenda por usuário
* Auditoria de ações
* Estratégia de backup automatizado
* Observabilidade centralizada

---

# 🧑‍💻 Desenvolvimento

## Padrões de código

* **Java 17**
* Controllers sem lógica de negócio
* DTOs separados das entidades
* Service Layer para regras de negócio
* Repository para persistência
* Injeção por construtor
* Exceções tratadas globalmente
* Commits atômicos e descritivos
* Configurações sensíveis mantidas fora do código

### Convenção de commits

O projeto pode utilizar [Conventional Commits](https://www.conventionalcommits.org/):

```bash
git commit -m "feat: adiciona endpoint de relatórios"
```

---

# 📞 Contato

| Canal          | Acesso                                                                                                 |
| -------------- | ------------------------------------------------------------------------------------------------------ |
| 🌐 **API**     | [agrogestor-api.duckdns.org](https://agrogestor-api.duckdns.org/)                                      |
| 📘 **Swagger** | [agrogestor-api.duckdns.org/swagger-ui.html](https://agrogestor-api.duckdns.org/swagger-ui.html)       |
| 🐛 **Issues**  | [github.com/ALVAROVELAME/agrogestor-api/issues](https://github.com/ALVAROVELAME/agrogestor-api/issues) |

---

### Feito com ☕ e 💚 no Brasil 🇧🇷

**AgroGestor API** — backend para gestão inteligente de rebanho

[⬆ Voltar ao topo](#-agrogestor-api)
