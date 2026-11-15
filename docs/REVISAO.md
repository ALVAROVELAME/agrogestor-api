# 🔎 Revisão técnica — AgroGestor API

Revisão realizada em **15/11/2026**, abrangendo os fontes Java, entidades, DTOs, repositórios, configuração Spring, Maven, Docker, pipeline e README. A revisão e os testes usam apenas ambiente local e banco descartável; não acessam os dados de produção.

## 🛠️ Problemas corrigidos

| Problema encontrado | Correção e verificação |
| --- | --- |
| Cadastro recebia DTO sem `@Valid` | Validação HTTP ativada; dados inválidos não chegam ao serviço. |
| Senhas de até 100 caracteres podiam ultrapassar o limite do BCrypt | Validação de até 72 **bytes UTF-8** no cadastro, login e exclusão; testes com ASCII, acentos e emoji. |
| Filtro JWT aceitava conta inativa | Exige `ativo=true`; teste HTTP com JWT real. |
| Token identificava conta apenas pelo e-mail | Confere o ID assinado e o e-mail contra a conta atual; token antigo não autentica uma conta recriada. |
| JWT era analisado duas vezes por requisição | Filtro valida e extrai claims em uma única operação, tratando tokens inválidos sem erro 500. |
| Configuração JWT aceitava duração não positiva ou emissor vazio | Falha explícita na inicialização; chaves curtas são rejeitadas pelo JJWT. |
| Login revelava situação de conta inativa antes de validar senha | Senha é verificada antes de informar falta de confirmação. |
| CORS aceitava qualquer subdomínio Vercel/DuckDNS com credenciais | Lista de origens exatas, incluindo `APP_FRONTEND_URL`; testes bloqueiam domínios de terceiros. |
| Requisições sem JWT recebiam status inadequado | Resposta 401 explícita para recursos protegidos. |
| Filtro poderia ser registrado no servlet container e no Spring Security | Registro automático no container desabilitado; execução pela cadeia de segurança. |
| Erros internos eram retornados como 400 com mensagem da exceção | Mensagem genérica com status 500; detalhes ficam no log do servidor. Erros HTTP do framework preservam seus status. |
| Animal ausente ou de outro dono não tinha status específico | Resposta 404, preservando o isolamento e sem revelar existência de animal alheio. |
| Conflitos de cadastro e integridade não tinham tratamento específico | Resposta 409 sem exposição de SQL ou detalhes internos. |
| Categorias dependiam de caixa, espaços e acentuação exatos | Normalização e mensagem de erro controlada; teste parametrizado. |
| Falha SMTP deixava cadastro pendente bloqueando nova tentativa | Cadastro transacional com rollback, verificado em MariaDB. |
| Cadastro expirado bloqueava permanentemente novo cadastro | Registro expirado pode ser substituído por novo token; tokens ainda válidos continuam protegidos contra substituição. |
| Confirmação podia criar usuário sem consumir o token atomicamente | Transação com bloqueio pessimista; testes de concorrência e rollback por trigger no MariaDB. |
| SpringDoc 2.x incompatível com Spring Boot 4 | SpringDoc 3.0.3 e testes reais de `/v3/api-docs` e Swagger UI. |
| Dependências transitivas com avisos de segurança | Spring Boot 4.0.9, MariaDB Connector/J 3.5.11 e JJWT 0.13.0; nova consulta OSV sem avisos para o grafo resolvido. |
| Build Docker incluía todo o diretório, inclusive arquivos locais sensíveis | `.dockerignore` exclui `.env`, chaves, Git, logs e artefatos locais; processo final usa UID/GID 10001. |
| Pipeline fazia deploy sem executar testes | Workflow reutilizável executa `clean verify`; deploy depende dos testes e usa o mesmo SHA. A imagem é construída antes da substituição do container, execuções de deploy são serializadas e a resposta HTTP é verificada com retry. |
| README indicava porta incorreta e um `.env.example` inexistente | Exemplo adicionado e documentação ajustada para `127.0.0.1:3002`, preservando a estrutura e identidade visual. |

SMTP também recebeu timeouts e exigência de STARTTLS; o Open Session in View foi desabilitado. A configuração `ddl-auto: update` foi preservada: adotar migrações versionadas exige planejar a transição do banco existente.

## 🧹 Arquivos duplicados ou sem uso

A comparação SHA-256 dos arquivos versionados não encontrou cópias byte a byte.

* Removido `UsuarioLoginDTO`: duplicava a finalidade de `LoginDTO` e não tinha consumidores.
* Removido `application.properties`: repetia exclusivamente o nome da aplicação já definido em `application.yml`.
* Movidos `AnimalRequestDTO` e `AnimalRespostaDTO` de `model/` para `dto/`, coerentemente com o pacote Java declarado.
* Removidos a dependência não utilizada de `CadastroPendenteRepository` em `AuthService`, a consulta não utilizada `UsuarioRepository.findByTokenConfirmacao`, o alias `CadastroPendente.getToken`, a variável local sem uso no cadastro e `app.base-url` sem consumidor.
* Removida a versão redundante do starter de validação e a repetição de `.idea` no `.gitignore`.
* Substituído o teste vazio de contexto dependente de credenciais externas por `AgrogestorApiIT`, com configuração isolada e cenários funcionais.

Os campos legados `Usuario.tokenConfirmacao` e `Usuario.tokenExpiraEm` foram preservados por serem persistidos e documentados. Retirá-los exige uma migração explícita do banco; a confirmação atual utiliza `CadastroPendente`.

## 🧪 Testes e reprodução

### Resultado local verificado

* **72 testes aprovados**: 64 unitários/HTTP/validação e 8 de integração, com zero falhas, erros ou testes ignorados.
* **JaCoCo:** 94,93% das linhas (412/434) e 87,04% dos ramos (47/54).
* **OSV:** 152 pacotes resolvidos consultados, sem avisos retornados após as atualizações.
* **Docker:** build concluído; imagem local `agrogestor-api:local-review` validada com usuário `10001:10001` e sem `.env` em `/app`.
* YAML dos workflows, Compose e configurações Spring validado sintaticamente; `git diff --check` sem erros. O workflow remoto e o deploy não foram executados nesta revisão.


* `./mvnw test`: testes de regras de negócio com Mockito, validação de entrada e testes HTTP com MockMvc e segurança habilitada.
* `./mvnw clean verify`: executa também os oito cenários de integração com MariaDB 11.4 em Docker e gera cobertura JaCoCo.
* No PowerShell, use `.\mvnw.cmd`.
* Relatórios em `target/surefire-reports`, `target/failsafe-reports` e `target/site/jacoco/index.html`.

O SMTP é simulado para impedir envio de mensagens reais. Os testes não verificam a entrega de e-mail por um provedor externo nem a configuração de Nginx, certificados ou máquinas de produção.

### JUnit 5 versus JUnit Jupiter 6

A tentativa de fixar Jupiter 5.14.4 gerou `NoSuchMethodError` em `ExtensionContext.Store.computeIfAbsent` ao carregar Spring Test. O Spring Framework 7 adota JUnit 6 como baseline. Foi mantido o Spring Boot 4 e utilizado o Jupiter gerenciado pelo BOM, com as mesmas anotações e o pacote `org.junit.jupiter`.

Fontes: [Spring Framework 7](https://spring.io/blog/2025/11/13/spring-framework-7-0-general-availability/), [compatibilidade SpringDoc](https://springdoc.org/#what-is-the-compatibility-matrix-of-springdoc-openapi-with-spring-boot-), [Testcontainers MariaDB](https://java.testcontainers.org/modules/databases/mariadb/).

## 🛡️ Verificação de dependências

O script `scripts/check_dependencies.py` consulta a API OSV usando nomes e versões dos artefatos Maven; não envia os fontes ou segredos. Ele inclui dependências transitivas e de teste, falha quando há avisos e também quando a consulta não pode ser concluída.

```powershell
.\mvnw.cmd dependency:tree "-DoutputType=json" "-DoutputFile=target/dependency-tree.json"
python scripts/check_dependencies.py
```

A primeira consulta encontrou avisos em oito artefatos: Log4j API, Jackson Core/Databind 2 e 3, Tomcat Embed Core, Spring Web MVC e MariaDB Connector/J. A atualização do BOM do Spring Boot corrigiu essas versões, com override do MariaDB para 3.5.11. Alguns avisos se referem a funcionalidades que a API não usa; os componentes foram atualizados mesmo assim.

Exemplos das fontes verificadas: [Log4j API](https://osv.dev/vulnerability/GHSA-qv9r-c865-cp47), [Jackson](https://osv.dev/vulnerability/GHSA-cxp5-3px4-pw24), [Tomcat](https://osv.dev/vulnerability/GHSA-9xv2-5v5q-p794), [Spring MVC](https://osv.dev/vulnerability/GHSA-pc63-qcmh-9cmg), [MariaDB Connector/J](https://osv.dev/vulnerability/GHSA-fhx5-2xqw-2cgm).

O relatório JSON identifica o momento da consulta e os resultados. Esta verificação não equivale a um pentest nem abrange vulnerabilidades da imagem base, do JDK instalado, do servidor de banco, do frontend ou da infraestrutura externa. Não é possível garantir a ausência de todos os bugs ou vulnerabilidades apenas com revisão estática e testes automatizados.
