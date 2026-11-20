package com.agrogestor.api;

import com.agrogestor.api.dto.UsuarioCadastroDTO;
import com.agrogestor.api.model.*;
import com.agrogestor.api.repository.*;
import com.agrogestor.api.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mariadb.MariaDBContainer;
import java.util.concurrent.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@org.springframework.test.annotation.DirtiesContext(classMode = org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS)
class AgrogestorApiIT {

    @Container
    @SuppressWarnings("resource")
    static final MariaDBContainer DATABASE = new MariaDBContainer("mariadb:11.4")
            .withDatabaseName("agrogestor_test").withUsername("teste").withPassword("teste");

    @DynamicPropertySource static void database(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DATABASE::getJdbcUrl);
        registry.add("spring.datasource.username", DATABASE::getUsername);
        registry.add("spring.datasource.password", DATABASE::getPassword);
    }
    @Autowired MockMvc mvc;
    @Autowired UsuarioRepository usuarios;
    @Autowired CadastroPendenteRepository pendentes;
    @Autowired AnimalRepository animais;
    @Autowired UsuarioService usuarioService;
    @Autowired EmailConfirmationService confirmationService;
    @Autowired JwtService jwt;
    @Autowired PasswordEncoder encoder;
    @Autowired JdbcTemplate jdbc;
    @MockitoBean JavaMailSender mailSender;

    @BeforeEach void limpar() {
        animais.deleteAll(); pendentes.deleteAll(); usuarios.deleteAll();
    }

    @Test void cadastroConfirmacaoLoginECrudCompleto() throws Exception {
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Maria\",\"email\":\"maria@example.com\",\"senha\":\"segredo\"}"))
                .andExpect(status().isOk());
        CadastroPendente cadastro = pendentes.findByEmail("maria@example.com").orElseThrow();
        assertThat(encoder.matches("segredo", cadastro.getSenhaHash())).isTrue();
        assertThat(usuarios.count()).isZero();
        verify(mailSender).send(any(SimpleMailMessage.class));
        mvc.perform(get("/api/auth/confirmar").param("token", cadastro.getTokenConfirmacao())).andExpect(status().isOk());
        mvc.perform(get("/api/auth/confirmar").param("token", cadastro.getTokenConfirmacao())).andExpect(status().isBadRequest());
        assertThat(pendentes.count()).isZero();
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"maria@example.com\",\"senha\":\"segredo\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.token").isNotEmpty());
        Usuario usuario = usuarios.findByEmail("maria@example.com").orElseThrow();
        String bearer = bearer(usuario);
        mvc.perform(post("/api/animais").header("Authorization", bearer).contentType(MediaType.APPLICATION_JSON).content(animalJson("Mimosa")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").isNumber());
        Animal animal = animais.findAll().get(0);
        mvc.perform(put("/api/animais/" + animal.getId()).header("Authorization", bearer).contentType(MediaType.APPLICATION_JSON).content(animalJson("Estrela")))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Estrela"));
        mvc.perform(get("/api/animais").header("Authorization", bearer))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].nome").value("Estrela"));
        mvc.perform(delete("/api/animais/" + animal.getId()).header("Authorization", bearer)).andExpect(status().isNoContent());
        assertThat(animais.count()).isZero();
    }

    @Test void novosRegistrosRelatoriosEIsolamentoNoBanco() throws Exception {
        Usuario dono = ativo("dono@example.com");
        Usuario outro = ativo("outro@example.com");
        Animal animal = animais.save(new Animal(dono, "A1", "Mimosa", Categoria.BEZERRA, 0.0));
        String token = bearer(dono);
        String producao = "{\"animalId\":" + animal.getId() + ",\"data\":\"2026-01-20\",\"turno\":\"MANHA\",\"litros\":18.5}";
        mvc.perform(post("/api/producoes").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(producao))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/producoes").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(producao))
                .andExpect(status().isConflict());
        mvc.perform(get("/api/producoes").header("Authorization", bearer(outro)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content.length()").value(0));
        mvc.perform(post("/api/producoes").header("Authorization", bearer(outro)).contentType(MediaType.APPLICATION_JSON).content(producao))
                .andExpect(status().isNotFound());
        mvc.perform(post("/api/financeiro/lancamentos").header("Authorization", token).contentType(MediaType.APPLICATION_JSON)
                .content("{\"data\":\"2026-01-20\",\"tipo\":\"DESPESA\",\"descricao\":\"Ração\",\"valor\":25.50}"))
                .andExpect(status().isCreated());
        mvc.perform(get("/api/relatorios/financeiro").header("Authorization", token).param("inicio", "2026-01-01").param("fim", "2026-01-31"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.saldo").value(-25.5));
        mvc.perform(get("/api/relatorios/producao").header("Authorization", token).param("inicio", "2026-01-01").param("fim", "2026-01-31"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].litros").value(18.5));
        mvc.perform(delete("/api/animais/" + animal.getId()).header("Authorization", token)).andExpect(status().isNoContent());
        mvc.perform(get("/api/producoes").header("Authorization", token)).andExpect(status().isOk()).andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test void tarefasEventosEFichaRespeitamUsuarioEAlimentamRelatorios() throws Exception {
        Usuario dono = ativo("dono@example.com");
        Usuario outro = ativo("outro@example.com");
        Animal animal = animais.save(new Animal(dono, "A1", "Mimosa", Categoria.BEZERRA, 0.0));
        String token = bearer(dono);
        mvc.perform(patch("/api/animais/" + animal.getId() + "/ficha").header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON).content("{\"raca\":\"Holandesa\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.raca").value("Holandesa"));
        mvc.perform(get("/api/animais/" + animal.getId()).header("Authorization", bearer(outro)))
                .andExpect(status().isNotFound());
        mvc.perform(post("/api/tarefas").header("Authorization", token).contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Vacinar\",\"data\":\"2026-01-20\"}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/eventos").header("Authorization", token).contentType(MediaType.APPLICATION_JSON)
                .content("{\"animalId\":" + animal.getId() + ",\"data\":\"2026-01-20\",\"tipo\":\"VACINACAO\",\"descricao\":\"Vacina\"}"))
                .andExpect(status().isCreated());
        mvc.perform(get("/api/eventos").header("Authorization", bearer(outro)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content.length()").value(0));
        mvc.perform(get("/api/indicadores").header("Authorization", token)
                .param("inicio", "2026-01-01").param("fim", "2026-01-31").param("referencia", "2026-01-31"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.tarefasPendentes").value(1))
                .andExpect(jsonPath("$.tarefasAtrasadas").value(1));
        mvc.perform(get("/api/relatorios/eventos").header("Authorization", token)
                .param("inicio", "2026-01-01").param("fim", "2026-01-31"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].tipo").value("VACINACAO"))
                .andExpect(jsonPath("$[0].quantidade").value(1));
    }

    @Test void isolamentoEntreUsuariosE404ParaAnimalAlheio() throws Exception {
        Usuario dono = ativo("dono@example.com"); Usuario outro = ativo("outro@example.com");
        Animal animal = animais.save(new Animal(dono, "A1", "Mimosa", Categoria.BEZERRA, 0.0));
        mvc.perform(get("/api/animais").header("Authorization", bearer(outro))).andExpect(status().isOk()).andExpect(content().json("[]"));
        mvc.perform(put("/api/animais/" + animal.getId()).header("Authorization", bearer(outro)).contentType(MediaType.APPLICATION_JSON).content(animalJson("Invadido")))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/api/animais/" + animal.getId()).header("Authorization", bearer(outro))).andExpect(status().isNotFound());
        assertThat(animais.findById(animal.getId()).orElseThrow().getNome()).isEqualTo("Mimosa");
    }

    @Test void recuperacaoAlteraSenhaUmaVezEInvalidaSessoesAnteriores() throws Exception {
        Usuario usuario = ativo("maria@example.com");
        String antigo = bearer(usuario);
        mvc.perform(post("/api/auth/forgot-password").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"maria@example.com\"}")).andExpect(status().isOk());
        org.mockito.ArgumentCaptor<SimpleMailMessage> email = org.mockito.ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(email.capture());
        String texto = email.getValue().getText();
        String token = texto.split("token=")[1].split("\\s")[0];
        String corpo = "{\"token\":\"" + token + "\",\"senha\":\"novaSenha123\"}";
        mvc.perform(post("/api/auth/reset-password").contentType(MediaType.APPLICATION_JSON).content(corpo)).andExpect(status().isOk());
        mvc.perform(post("/api/auth/reset-password").contentType(MediaType.APPLICATION_JSON).content(corpo)).andExpect(status().isBadRequest());
        mvc.perform(get("/api/auth/me").header("Authorization", antigo)).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"maria@example.com\",\"senha\":\"novaSenha123\"}")).andExpect(status().isOk());
    }

    @Test void exclusaoRemoveSomenteDadosDoDonoEInvalidaToken() throws Exception {
        Usuario dono = ativo("dono@example.com"); Usuario outro = ativo("outro@example.com");
        animais.save(new Animal(dono, "A1", "Mimosa", Categoria.BEZERRA, 0.0));
        animais.save(new Animal(outro, "A2", "Estrela", Categoria.BEZERRA, 0.0));
        String token = bearer(dono);
        mvc.perform(delete("/api/usuarios/me").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized());
        assertThat(animais.count()).isEqualTo(2);
        mvc.perform(delete("/api/usuarios/me").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content("{\"senha\":\"segredo\"}"))
                .andExpect(status().isOk());
        assertThat(usuarios.findById(dono.getId())).isEmpty();
        assertThat(animais.findByUsuarioIdOrderByCriadoEmDesc(outro.getId())).hasSize(1);
        mvc.perform(get("/api/auth/me").header("Authorization", token)).andExpect(status().isUnauthorized());
        ativo("dono@example.com");
        mvc.perform(get("/api/auth/me").header("Authorization", token)).andExpect(status().isUnauthorized());
    }

    @Test void falhaSmtpNaoDeixaCadastroPreso() {
        doThrow(new MailSendException("SMTP indisponível")).when(mailSender).send(any(SimpleMailMessage.class));
        assertThatThrownBy(() -> usuarioService.cadastrar(cadastro())).isInstanceOf(MailSendException.class);
        assertThat(pendentes.count()).isZero();
    }

    @Test void cadastroExpiradoPodeSerRenovado() {
        CadastroPendente antigo = pendentes.save(new CadastroPendente("Maria", "maria@example.com", "hash", "antigo"));
        jdbc.update("update cadastros_pendentes set token_expira_em = ? where id = ?", java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().minusDays(1)), antigo.getId());
        assertThat(confirmationService.confirmarEmail("antigo")).isFalse();
        CadastroPendente novo = usuarioService.cadastrar(cadastro());
        assertThat(novo.getTokenConfirmacao()).isNotEqualTo("antigo");
        assertThat(pendentes.count()).isEqualTo(1);
        assertThat(confirmationService.confirmarEmail(novo.getTokenConfirmacao())).isTrue();
    }

    @Test void confirmacoesConcorrentesConsomemTokenUmaVez() throws Exception {
        pendentes.save(new CadastroPendente("Maria", "maria@example.com", "hash", "unico"));
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch inicio = new CountDownLatch(1);
        try {
            Callable<Boolean> confirmar = () -> { inicio.await(); return confirmationService.confirmarEmail("unico"); };
            Future<Boolean> primeira = executor.submit(confirmar); Future<Boolean> segunda = executor.submit(confirmar);
            inicio.countDown();
            assertThat(java.util.List.of(primeira.get(20, TimeUnit.SECONDS), segunda.get(20, TimeUnit.SECONDS)))
                    .containsExactlyInAnyOrder(true, false);
            assertThat(usuarios.count()).isEqualTo(1); assertThat(pendentes.count()).isZero();
        } finally { executor.shutdownNow(); }
    }

    @Test void falhaNaConfirmacaoReverteCriacaoDoUsuario() {
        pendentes.save(new CadastroPendente("Maria", "maria@example.com", "hash", "rollback"));
        jdbc.execute("CREATE TRIGGER impedir_exclusao BEFORE DELETE ON cadastros_pendentes FOR EACH ROW SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'falha simulada'");
        try {
            assertThatThrownBy(() -> confirmationService.confirmarEmail("rollback")).isInstanceOf(RuntimeException.class);
            assertThat(usuarios.count()).isZero(); assertThat(pendentes.count()).isEqualTo(1);
        } finally { jdbc.execute("DROP TRIGGER impedir_exclusao"); }
    }

    @Test void swaggerEOpenApiCarregam() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk()).andExpect(jsonPath("$.paths['/api/animais']").exists());
        mvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }

    private Usuario ativo(String email) {
        Usuario u = new Usuario("Maria", email, encoder.encode("segredo")); u.ativarConta(); return usuarios.save(u);
    }
    private String bearer(Usuario u) { return "Bearer " + jwt.gerarToken(u); }
    private UsuarioCadastroDTO cadastro() {
        UsuarioCadastroDTO dto = new UsuarioCadastroDTO(); dto.setNome("Maria"); dto.setEmail("maria@example.com"); dto.setSenha("segredo"); return dto;
    }
    private String animalJson(String nome) { return "{\"brinco\":\"A1\",\"nome\":\"" + nome + "\",\"categoria\":\"BEZERRA\",\"producaoDiaria\":0.0}"; }
}
