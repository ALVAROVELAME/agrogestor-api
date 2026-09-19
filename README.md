<div align="center">

# 🐄 AgroGestor API

### Backend REST para gestão de rebanho leiteiro

API em Spring Boot com autenticação JWT, confirmação de e-mail e isolamento multi-tenant por usuário.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.7-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org)
[![MariaDB](https://img.shields.io/badge/MariaDB-11-003545?logo=mariadb&logoColor=white)](https://mariadb.org)
[![JWT](https://img.shields.io/badge/JWT-jjwt--0.12.6-000000?logo=jsonwebtokens&logoColor=white)](https://github.com/jwtk/jjwt)
[![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)](https://www.docker.com)
[![License](https://img.shields.io/badge/license-MIT-green)](./LICENSE)

[🌐 API em produção](https://agrogestor-api.duckdns.org) · [📘 Swagger UI](https://agrogestor-api.duckdns.org/swagger-ui.html)

</div>

---

## 📖 Sobre o projeto

O **AgroGestor API** é o backend do sistema AgroGestor — uma aplicação de gestão de rebanho leiteiro. Fornece endpoints REST para autenticação, cadastro de usuários, confirmação de e-mail e CRUD de animais, com isolamento total de dados entre contas.

### Diferenciais

- 🔐 **JWT stateless** com filtro de autenticação customizado
- 📧 **Confirmação de e-mail** com token expirável (24h)
- 🛡️ **Multi-tenant seguro** — cada usuário só enxerga seus próprios dados
- 🗄️ **JPA + Hibernate** com `ddl-auto: update` para evolução do schema
- 📚 **Swagger/OpenAPI 3.1** auto-gerado
- 🐳 **Docker pronto** com limitação de memória (300M) e CPU (0.5)
- 🚀 **Deploy automatizado** via GitHub Actions + self-hosted runner
- ✅ **Validação de entrada** com Bean Validation
- 🎯 **Tratamento global de exceções** com respostas padronizadas

---

## ✨ Funcionalidades

### Autenticação e conta

- ✅ Cadastro com validação (nome, e-mail, senha)
- ✅ Confirmação de e-mail por token único (expira em 24h)
- ✅ Login com JWT (expiração configurável)
- ✅ Endpoint `/me` para consultar usuário logado
- ✅ Exclusão de conta com confirmação por senha
- ✅ Senhas armazenadas com **BCrypt**

### Rebanho (animais)

- ✅ Listagem por usuário autenticado
- ✅ Criação com validação completa
- ✅ Atualização por ID
- ✅ Exclusão por ID
- ✅ Isolamento automático: usuário nunca vê animal de outro

---

## 🛠 Stack técnica

### Core

| Tecnologia | Versão | Uso |
|---|---|---|
| [Spring Boot](https://spring.io/projects/spring-boot) | 4.0.7 | Framework |
| [Java](https://openjdk.org) | 17 | Linguagem |
| [Spring Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html) | — | Camada REST |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa) | — | Persistência |
| [Hibernate](https://hibernate.org) | 6.x | ORM |
| [MariaDB JDBC](https://mariadb.com/kb/en/about-mariadb-connector-j/) | — | Driver |

### Segurança e autenticação

| Tecnologia | Versão | Uso |
|---|---|---|
| [Spring Security](https://spring.io/projects/spring-security) | — | Filtros de auth |
| [JJWT](https://github.com/jwtk/jjwt) | 0.12.6 | Geração/validação JWT |
| [BCrypt](https://en.wikipedia.org/wiki/Bcrypt) | — | Hash de senhas |

### Comunicação

| Tecnologia | Versão | Uso |
|---|---|---|
| [Spring Mail](https://docs.spring.io/spring-framework/reference/integration/email.html) | — | Envio de e-mails |
| [SpringDoc OpenAPI](https://springdoc.org) | 2.8.6 | Swagger UI |
| [Bean Validation](https://beanvalidation.org) | — | Validação de DTOs |

### DevOps

| Ferramenta | Uso |
|---|---|
| [Maven Wrapper](https://maven.apache.org/wrapper/) | Build reproduzível |
| [Docker](https://www.docker.com) | Containerização |
| [Docker Compose](https://docs.docker.com/compose/) | Orquestração local |
| [GitHub Actions](https://github.com/features/actions) | CI/CD com self-hosted runner |

---

## 📁 Estrutura do projeto

```
agrogestor-api/
├── .github/
│   └── workflows/
│       └── deploy.yml             # CI/CD com self-hosted runner
├── .mvn/wrapper/                  # Maven Wrapper
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
│   └── test/java/com/agrogestor/api/
│       └── AgrogestorApiApplicationTests.java
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── mvnw
└── mvnw.cmd
```

---

## 🌐 Endpoints da API

### Base URL

| Ambiente | URL |
|---|---|
| **Produção** | `https://agrogestor-api.duckdns.org` |
| **Local** | `http://localhost:8080` |

### 🔓 Públicos (sem token)

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/` | Health check — confirma que a API está no ar |
| `POST` | `/api/auth/login` | Autenticação (retorna JWT) |
| `GET` | `/api/auth/confirmar?token=...` | Confirma e-mail do usuário |
| `POST` | `/api/usuarios` | Inicia cadastro (envia e-mail de confirmação) |

### 🔐 Protegidos (Bearer Token)

Requerem header `Authorization: Bearer <token>`.

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/auth/me` | Retorna dados do usuário logado |
| `DELETE` | `/api/usuarios/me` | Exclui conta (requer senha no body) |
| `GET` | `/api/animais` | Lista animais do usuário logado |
| `POST` | `/api/animais` | Cria animal |
| `PUT` | `/api/animais/{id}` | Atualiza animal |
| `DELETE` | `/api/animais/{id}` | Exclui animal |

### 📘 Documentação interativa

Swagger UI disponível em produção:

```
https://agrogestor-api.duckdns.org/swagger-ui.html
```

OpenAPI 3.1 JSON:

```
https://agrogestor-api.duckdns.org/v3/api-docs
```

---

## 📡 Exemplos de requisição

### 1. Cadastro

```bash
curl -X POST https://agrogestor-api.duckdns.org/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João da Silva",
    "email": "joao@example.com",
    "senha": "senhaSegura123"
  }'
```

**Resposta:**
```json
{
  "sucesso": true,
  "mensagem": "Cadastro iniciado. Verifique seu email para confirmar a conta."
}
```

---

### 2. Login

```bash
curl -X POST https://agrogestor-api.duckdns.org/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "senha": "senhaSegura123"
  }'
```

**Resposta:**
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

### 3. Criar animal

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

**Categorias aceitas:** `BEZERRA`, `NOVILHA`, `VACA_EM_LACTACAO`, `VACA_SECA`

---

### 4. Excluir conta

```bash
curl -X DELETE https://agrogestor-api.duckdns.org/api/usuarios/me \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{ "senha": "senhaSegura123" }'
```

---

## 🗄 Modelo de dados

### `usuarios`

| Coluna | Tipo | Constraints |
|---|---|---|
| `id` | BIGINT | PK, auto-increment |
| `nome` | VARCHAR | NOT NULL |
| `email` | VARCHAR | NOT NULL, UNIQUE |
| `senha_hash` | VARCHAR | NOT NULL (BCrypt) |
| `ativo` | BOOLEAN | NOT NULL, default false |
| `token_confirmacao` | VARCHAR | UNIQUE, nullable |
| `token_expira_em` | DATETIME | nullable |
| `email_confirmado_em` | DATETIME | nullable |
| `criado_em` | DATETIME | NOT NULL |

### `animais`

| Coluna | Tipo | Constraints |
|---|---|---|
| `id` | BIGINT | PK, auto-increment |
| `usuario_id` | BIGINT | FK → usuarios.id, NOT NULL |
| `brinco` | VARCHAR | NOT NULL |
| `nome` | VARCHAR | NOT NULL |
| `categoria` | VARCHAR | ENUM: BEZERRA, NOVILHA, VACA_EM_LACTACAO, VACA_SECA |
| `producao_diaria` | DOUBLE | NOT NULL |
| `criado_em` | DATETIME | NOT NULL |
| `atualizado_em` | DATETIME | NOT NULL |

### `cadastros_pendentes`

| Coluna | Tipo | Constraints |
|---|---|---|
| `id` | BIGINT | PK, auto-increment |
| `nome` | VARCHAR | NOT NULL |
| `email` | VARCHAR | NOT NULL, UNIQUE |
| `senha_hash` | VARCHAR | NOT NULL |
| `token_confirmacao` | VARCHAR | NOT NULL, UNIQUE |
| `token_expira_em` | DATETIME | NOT NULL |
| `criado_em` | DATETIME | NOT NULL |

---

## 🔐 Fluxo de autenticação

```
┌─────────────────────────────────────────────────────────────┐
│  1. CADASTRO                                                │
├─────────────────────────────────────────────────────────────┤
│  POST /api/usuarios                                         │
│      ↓                                                      │
│  UsuarioService.cadastrar()                                 │
│      ↓                                                      │
│  Cria CadastroPendente + gera token UUID                    │
│      ↓                                                      │
│  EmailService envia link:                                   │
│      https://frontend.com/confirmar?token=<uuid>            │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  2. CONFIRMAÇÃO                                             │
├─────────────────────────────────────────────────────────────┤
│  GET /api/auth/confirmar?token=<uuid>                       │
│      ↓                                                      │
│  EmailConfirmationService.confirmarEmail()                  │
│      ↓                                                      │
│  Valida token + expiração (24h)                             │
│      ↓                                                      │
│  Cria Usuario ativo + remove CadastroPendente               │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  3. LOGIN                                                   │
├─────────────────────────────────────────────────────────────┤
│  POST /api/auth/login                                       │
│      ↓                                                      │
│  AuthService.autenticar() — valida BCrypt                   │
│      ↓                                                      │
│  JwtService.gerarToken() — assina com HMAC-SHA256           │
│      ↓                                                      │
│  Retorna { token, usuario }                                 │
└─────────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────────┐
│  4. REQUISIÇÕES AUTENTICADAS                                │
├─────────────────────────────────────────────────────────────┤
│  Authorization: Bearer <token>                              │
│      ↓                                                      │
│  JwtAuthenticationFilter (OncePerRequestFilter)             │
│      ↓                                                      │
│  Valida assinatura + extrai email                           │
│      ↓                                                      │
│  Carrega Usuario e popula SecurityContext                   │
│      ↓                                                      │
│  Controller recebe via @AuthenticationPrincipal             │
└─────────────────────────────────────────────────────────────┘
```

---

## 🚀 Como rodar localmente

### Pré-requisitos

- **Java 17** (JDK)
- **Docker** + **Docker Compose** (recomendado) — ou MariaDB local
- **Git**

### Opção 1 — Docker Compose (recomendado)

**1. Clone o repositório:**

```bash
git clone https://github.com/ALVAROVELAME/agrogestor-api.git
cd agrogestor-api
```

**2. Crie o `.env` na raiz** (veja a seção [Variáveis de ambiente](#-variáveis-de-ambiente)):

```bash
cp .env.example .env
# edite o .env com seus valores
```

**3. Suba o container:**

```bash
docker compose up -d --build
```

**4. Acompanhe os logs:**

```bash
docker compose logs -f agrogestor_api
```

**5. Teste:**

```bash
curl http://localhost:3002/
# → "API AgroGestor funcionando"
```

### Opção 2 — Maven direto

```bash
./mvnw clean package -DskipTests
java -jar target/agrogestor-api-0.0.1-SNAPSHOT.jar
```

> ⚠️ Requer MariaDB acessível com as credenciais configuradas no `.env`.

---

## ⚙️ Variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com:

```env
# ===== BANCO DE DADOS =====
DB_HOST=mariadb
DB_PORT=3306
DB_NAME=agrogestor
DB_USER=agrogestor
DB_PASSWORD=sua_senha_forte_aqui

# ===== JWT =====
JWT_SECRET=sua_chave_secreta_com_pelo_menos_32_caracteres
JWT_EXPIRATION=86400000
JWT_ISSUER=agrogestor-api

# ===== EMAIL (SMTP) =====
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seu_email@gmail.com
MAIL_PASSWORD=sua_senha_de_app_aqui
MAIL_FROM=AgroGestor <nao-responda@agrogestor.app>

# ===== URLs DA APLICAÇÃO =====
APP_BASE_URL=https://agrogestor-api.duckdns.org
APP_FRONTEND_URL=https://agrogestor-br.vercel.app

# ===== PORTA DO SERVIDOR =====
PORT=8080
```

### ⚠️ Notas importantes

- **`JWT_SECRET`** deve ter **no mínimo 32 caracteres** (exigência do `jjwt` para HMAC-SHA256)
- **`MAIL_PASSWORD`** no Gmail é uma **senha de app**, não a senha da conta
- **`APP_FRONTEND_URL`** é usada no link do e-mail de confirmação — deve apontar para o frontend, não para a API

---

## 🐳 Docker

### Dockerfile (multi-stage)

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

### docker-compose.yml

```yaml
services:
  agrogestor_api:
    build: .
    container_name: agrogestor_api
    env_file:
      - .env
    ports:
      - "127.0.0.1:3002:8080"
    restart: unless-stopped
    deploy:
      resources:
        limits:
          memory: 300M
          cpus: "0.50"
```

**Comandos úteis:**

```bash
# Subir
docker compose up -d --build

# Logs
docker compose logs -f agrogestor_api

# Parar
docker compose down

# Reconstruir sem cache
docker compose build --no-cache

# Ver consumo
docker stats --no-stream
```

---

## 🚢 Deploy automatizado

O projeto usa **GitHub Actions com self-hosted runner** para deploy automático. Veja `.github/workflows/deploy.yml`.

### Fluxo do deploy

```
Push em main
      ↓
GitHub Actions aciona o self-hosted runner
      ↓
1. Verifica ambiente (docker, memória, disco)
      ↓
2. Atualiza projeto em /srv/apps/agrogestor-api
      ↓
3. Valida estrutura (Dockerfile, docker-compose, pom.xml)
      ↓
4. Para container antigo (docker compose down)
      ↓
5. Builda nova imagem (com cache)
      ↓
6. Sobe container (docker compose up -d)
      ↓
7. Aguarda 25s e verifica logs
      ↓
8. Limpa imagens órfãs
```

### Configuração do runner

O runner precisa estar registrado no repositório GitHub com as labels `self-hosted` e acesso a `/srv/apps/`.

---

## 🧪 Testes

O projeto usa **JUnit 5** com **Spring Boot Test**.

### Rodar todos os testes

```bash
./mvnw test
```

### Rodar um teste específico

```bash
./mvnw test -Dtest=AgrogestorApiApplicationTests
```

### Rodar com cobertura (opcional)

Adicione o plugin JaCoCo no `pom.xml` para gerar relatório em `target/site/jacoco/index.html`.

---

## 🏗 Arquitetura em camadas

```
┌────────────────────────────────────────────────┐
│               Controller (REST)                │
│  - Recebe HTTP, valida, delega para Service    │
│  - Usa @AuthenticationPrincipal p/ usuário     │
└───────────────────────┬────────────────────────┘
                        │
┌───────────────────────▼────────────────────────┐
│                  Service (BLL)                 │
│  - Regras de negócio, orquestração             │
│  - Transações (@Transactional)                 │
│  - Lança exceções de domínio                   │
└───────────────────────┬────────────────────────┘
                        │
┌───────────────────────▼────────────────────────┐
│              Repository (JPA)                  │
│  - Queries, CRUD, isolamento por usuário       │
│  - extends JpaRepository<T, ID>                │
└───────────────────────┬────────────────────────┘
                        │
┌───────────────────────▼────────────────────────┐
│              Model (Entidades)                 │
│  - @Entity, relacionamentos                    │
│  - Ciclo de vida (@PreUpdate)                  │
└────────────────────────────────────────────────┘
```

### Padrões usados

| Padrão | Onde |
|---|---|
| **DTO** | Pacote `dto/` — separa API do domínio |
| **Repository** | Spring Data JPA |
| **Service Layer** | Lógica de negócio |
| **Dependency Injection** | Construtor (recomendado pelo Spring) |
| **Exception Handler Global** | `@RestControllerAdvice` |
| **Stateless Auth** | JWT + `SessionCreationPolicy.STATELESS` |

---

## ♿ Segurança

### Implementado

- ✅ **BCrypt** para hash de senhas (força 10 por padrão)
- ✅ **JWT HMAC-SHA256** com assinatura verificada
- ✅ **Stateless** — sem sessão no servidor
- ✅ **CORS** configurado com origens explícitas
- ✅ **CSRF desabilitado** (API stateless — correto)
- ✅ **Isolamento de dados** por usuário no nível da query
- ✅ **Validação de entrada** em todos os DTOs
- ✅ **Senha obrigatória** para excluir conta (proteção contra CSRF)
- ✅ **Token expirável** (24h para confirmação, 24h para JWT por padrão)
- ✅ **Secret JWT** com mínimo de 32 caracteres
- ✅ **HTTPS obrigatório** em produção (via `forward-headers-strategy: framework`)

### Recomendações para produção

- 🔐 Use HTTPS em todos os endpoints (Let's Encrypt, Cloudflare, etc.)
- 🔑 Rotacione o `JWT_SECRET` periodicamente
- 📧 Use um provedor de e-mail transacional (SendGrid, Postmark, SES)
- 🛡️ Configure rate limiting (nginx, Cloudflare)
- 📊 Monitore logs com Sentry, Datadog, etc.
- 🗄️ Faça backup regular do MariaDB

---

## 📊 Logs e monitoramento

### Ver logs do container

```bash
docker logs --tail 100 agrogestor_api
docker logs -f agrogestor_api
```

### Estatísticas em tempo real

```bash
docker stats agrogestor_api
```

### Health check

```bash
curl -s https://agrogestor-api.duckdns.org/ | head -c 100
# → "API AgroGestor funcionando"
```

---

## 🗺 Roadmap

### Concluído ✅

- [x] Cadastro com confirmação de e-mail
- [x] Login com JWT
- [x] CRUD completo de animais
- [x] Isolamento multi-tenant
- [x] Exclusão de conta
- [x] Swagger/OpenAPI
- [x] Dockerização
- [x] Deploy automatizado com GitHub Actions
- [x] Tratamento global de exceções

### Em planejamento 📋

- [ ] Refresh token
- [ ] Rate limiting por IP
- [ ] Logs estruturados (JSON)
- [ ] Métricas Prometheus/Grafana
- [ ] Testes de integração com Testcontainers
- [ ] Migrations com Flyway
- [ ] Multi-fazenda por usuário
- [ ] Auditoria de ações (`@CreatedBy`, `@LastModifiedBy`)

---

## 🤝 Contribuição

1. Faça um **fork** do projeto
2. Crie uma branch para sua feature:
   ```bash
   git checkout -b feat/minha-feature
   ```
3. Commit seguindo [Conventional Commits](https://www.conventionalcommits.org/):
   ```bash
   git commit -m "feat: adiciona endpoint de relatórios"
   ```
4. Faça push:
   ```bash
   git push origin feat/minha-feature
   ```
5. Abra um **Pull Request**

### Padrões de código

- **Java 17** com recursos modernos (records, switch expressions, text blocks)
- **Camadas bem definidas** — Controller → Service → Repository
- **DTOs separados** dos Models JPA
- **Sem lógica de negócio em Controllers**
- **Exceções de domínio** para erros esperados
- **Injeção por construtor** (nunca `@Autowired` em campo)
- **Commits atômicos** com mensagens descritivas

---

## 📄 Licença

Este projeto está sob a licença **MIT**. Veja o arquivo [LICENSE](./LICENSE) para mais detalhes.

---

## 📞 Contato

| Canal | |
|---|---|
| 🌐 **API** | [agrogestor-api.duckdns.org](https://agrogestor-api.duckdns.org) |
| 📘 **Swagger** | [agrogestor-api.duckdns.org/swagger-ui.html](https://agrogestor-api.duckdns.org/swagger-ui.html) |
| 🐛 **Issues** | [github.com/ALVAROVELAME/agrogestor-api/issues](https://github.com/ALVAROVELAME/agrogestor-api/issues) |

---

<div align="center">

### Feito com ☕ e 💚 no Brasil 🇧🇷

**AgroGestor API** — backend para gestão inteligente de rebanho

[⬆ Voltar ao topo](#-agrogestor-api)

</div>