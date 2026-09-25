# 🐄 AgroGestor API

### Backend REST para gestão de rebanho leiteiro

API REST desenvolvida com Spring Boot para gerenciamento de rebanho leiteiro, com autenticação JWT, confirmação de e-mail e isolamento de dados por usuário.

[Spring Boot](https://spring.io/projects/spring-boot) ([image](https://img.shields.io/badge/Spring%20Boot-4.0.7-6DB33F?logo=springboot&logoColor=white))
[Java](https://openjdk.org/) ([image](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white))
[MariaDB](https://mariadb.org/) ([image](https://img.shields.io/badge/MariaDB-11-003545?logo=mariadb&logoColor=white))
[JWT](https://github.com/jwtk/jjwt) ([image](https://img.shields.io/badge/JWT-jjwt--0.12.6-000000?logo=jsonwebtokens&logoColor=white))
[Docker](https://www.docker.com/) ([image](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white))

[🌐 API em produção](https://agrogestor-api.duckdns.org/) · [📘 Swagger UI](https://agrogestor-api.duckdns.org/swagger-ui.html)

---

## 📖 Sobre o projeto

O **AgroGestor API** é o backend do sistema AgroGestor, uma aplicação voltada à gestão de rebanho leiteiro.

A API fornece recursos para autenticação, cadastro de usuários, confirmação de e-mail e gerenciamento de animais, utilizando uma arquitetura REST organizada em camadas e isolamento de dados por usuário.

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

### Rebanho

* ✅ Listagem de animais do usuário autenticado
* ✅ Criação de animais com validação
* ✅ Atualização por ID
* ✅ Exclusão por ID
* ✅ Isolamento de dados entre usuários

---

# 🛠️ Stack técnica

## Core

| Tecnologia                                                                          | Versão | Uso                 |
| ----------------------------------------------------------------------------------- | -----: | ------------------- |
| [Spring Boot](https://spring.io/projects/spring-boot)                               |  4.0.7 | Framework principal |
| [Java](https://openjdk.org/)                                                        |     17 | Linguagem           |
| [Spring Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html) |      — | Camada REST         |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa)                       |      — | Persistência        |
| [Hibernate](https://hibernate.org/)                                                 |    6.x | ORM                 |
| [MariaDB Connector/J](https://mariadb.com/kb/en/about-mariadb-connector-j/)         |      — | Driver JDBC         |

## Segurança e autenticação

| Tecnologia                                                    | Versão | Uso                        |
| ------------------------------------------------------------- | -----: | -------------------------- |
| [Spring Security](https://spring.io/projects/spring-security) |      — | Segurança e filtros        |
| [JJWT](https://github.com/jwtk/jjwt)                          | 0.12.6 | Geração e validação de JWT |
| [BCrypt](https://en.wikipedia.org/wiki/Bcrypt)                |      — | Hash de senhas             |

## Comunicação e documentação

| Tecnologia                                                                              | Versão | Uso                  |
| --------------------------------------------------------------------------------------- | -----: | -------------------- |
| [Spring Mail](https://docs.spring.io/spring-framework/reference/integration/email.html) |      — | Envio de e-mails     |
| [SpringDoc OpenAPI](https://springdoc.org/)                                             |  2.8.6 | Swagger UI / OpenAPI |
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
├── .github/
│   └── workflows/
│       └── deploy.yml
├── .mvn/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/com/agrogestor/api/
│   │   │   ├── AgrogestorApiApplication.java
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── JwtProperties.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── SecurityFilterConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AnimalController.java
│   │   │   │   ├── ApiController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   └── UsuarioController.java
│   │   │   ├── dto/
│   │   │   │   ├── AnimalRequestDTO.java
│   │   │   │   ├── AnimalRespostaDTO.java
│   │   │   │   ├── ExcluirContaDTO.java
│   │   │   │   ├── LoginDTO.java
│   │   │   │   ├── LoginRespostaDTO.java
│   │   │   │   ├── MensagemRespostaDTO.java
│   │   │   │   ├── UsuarioCadastroDTO.java
│   │   │   │   └── UsuarioRespostaDTO.java
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── model/
│   │   │   │   ├── Animal.java
│   │   │   │   ├── CadastroPendente.java
│   │   │   │   ├── Categoria.java
│   │   │   │   └── Usuario.java
│   │   │   ├── repository/
│   │   │   │   ├── AnimalRepository.java
│   │   │   │   ├── CadastroPendenteRepository.java
│   │   │   │   └── UsuarioRepository.java
│   │   │   ├── security/
│   │   │   │   └── JwtAuthenticationFilter.java
│   │   │   └── service/
│   │   │       ├── AnimalService.java
│   │   │       ├── AuthService.java
│   │   │       ├── EmailConfirmationService.java
│   │   │       ├── EmailService.java
│   │   │       ├── JwtService.java
│   │   │       └── UsuarioService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application.yml
│   └── test/
│       └── java/com/agrogestor/api/
│           └── AgrogestorApiApplicationTests.java
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
| **Local**    | `http://localhost:8080`              |

## 🔓 Públicos

| Método | Endpoint                        | Descrição             |
| ------ | ------------------------------- | --------------------- |
| `GET`  | `/`                             | Health check          |
| `POST` | `/api/auth/login`               | Autenticação          |
| `GET`  | `/api/auth/confirmar?token=...` | Confirmação de e-mail |
| `POST` | `/api/usuarios`                 | Inicia cadastro       |

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
* **Docker**
* **Docker Compose**
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

Edite os valores de acordo com seu ambiente local.

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
curl http://localhost:8080/
```

Resposta esperada:

```text
API AgroGestor funcionando
```

---

## Opção 2 — Maven

```bash
./mvnw clean package -DskipTests
java -jar target/agrogestor-api-0.0.1-SNAPSHOT.jar
```

Essa opção requer um MariaDB acessível e configurado no ambiente local.

---

# ⚙️ Variáveis de ambiente

Exemplo de configuração local:

```env
# ===== BANCO DE DADOS =====
DB_HOST=mariadb
DB_PORT=3306
DB_NAME=agrogestor
DB_USER=agrogestor
DB_PASSWORD=defina_uma_senha_local

# ===== JWT =====
JWT_SECRET=defina_uma_chave_secreta_forte
JWT_EXPIRATION=86400000
JWT_ISSUER=agrogestor-api

# ===== EMAIL =====
MAIL_HOST=smtp.exemplo.com
MAIL_PORT=587
MAIL_USERNAME=seu_email@exemplo.com
MAIL_PASSWORD=sua_credencial_local
MAIL_FROM=AgroGestor <seu_email@exemplo.com>

# ===== APLICAÇÃO =====
APP_BASE_URL=http://localhost:8080
APP_FRONTEND_URL=http://localhost:3000

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

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
```

## Exemplo de Docker Compose para ambiente local

```yaml
services:
  agrogestor_api:
    build: .
    env_file:
      - .env

    ports:
      - "8080:8080"

    restart: unless-stopped

    deploy:
      resources:
        limits:
          memory: 300M
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

O projeto utiliza **JUnit 5** e **Spring Boot Test**.

## Todos os testes

```bash
./mvnw test
```

## Teste específico

```bash
./mvnw test -Dtest=AgrogestorApiApplicationTests
```

## Cobertura

Para adicionar cobertura, o projeto pode utilizar JaCoCo e gerar:

```text
target/site/jacoco/index.html
```

---

# 🔒 Segurança

## Implementado

* ✅ BCrypt para armazenamento de senhas
* ✅ JWT assinado com HMAC-SHA256
* ✅ Autenticação stateless
* ✅ CORS configurado
* ✅ CSRF desabilitado para API stateless
* ✅ Isolamento de dados por usuário
* ✅ Validação de DTOs
* ✅ Confirmação de senha para exclusão de conta
* ✅ Expiração de tokens
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

## Em planejamento 📋

* Refresh token
* Rate limiting por IP
* Logs estruturados em JSON
* Métricas com Prometheus/Grafana
* Testes de integração com Testcontainers
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
