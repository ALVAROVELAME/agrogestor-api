package com.agrogestor.api.controller;

import com.agrogestor.api.config.*;
import com.agrogestor.api.model.*;
import com.agrogestor.api.repository.UsuarioRepository;
import com.agrogestor.api.security.JwtAuthenticationFilter;
import com.agrogestor.api.service.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ApiController.class, AuthController.class, UsuarioController.class, AnimalController.class, RecuperacaoSenhaController.class},
        properties = {"app.frontend-url=https://frontend.example.com", "jwt.secret=chave-exclusiva-de-testes-com-mais-de-32-bytes", "jwt.issuer=teste", "jwt.expiration=60000"})
@Import({SecurityFilterConfig.class, CorsConfig.class, JwtAuthenticationFilter.class, JwtService.class, JwtProperties.class})
class ApiHttpTest {
    @Autowired MockMvc mvc;
    @Autowired JwtService jwt;
    @MockitoBean UsuarioRepository usuarios;
    @MockitoBean UsuarioService usuarioService;
    @MockitoBean AuthService authService;
    @MockitoBean AnimalService animalService;
    @MockitoBean EmailConfirmationService confirmationService;
    @MockitoBean RecuperacaoSenhaService recuperacaoSenhaService;
    Usuario usuario;
    @BeforeEach void preparar() {
        usuario = new Usuario("Maria", "maria@example.com", "hash-secreto");
        ReflectionTestUtils.setField(usuario, "id", 7L); usuario.ativarConta();
    }
    String bearer() {
        when(usuarios.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
        return "Bearer " + jwt.gerarToken(usuario);
    }
    @Test void statusPublico() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string("API AgroGestor funcionando"));
    }
    @ParameterizedTest @ValueSource(strings = {"/api/animais", "/api/auth/me"})
    void exigeAutenticacao(String path) throws Exception { mvc.perform(get(path)).andExpect(status().isUnauthorized()); }
    @Test void tokenInvalidoRetorna401() throws Exception {
        mvc.perform(get("/api/animais").header("Authorization", "Bearer lixo")).andExpect(status().isUnauthorized());
        verifyNoInteractions(animalService);
    }
    @Test void contaInativaNaoPodeUsarJwt() throws Exception {
        ReflectionTestUtils.setField(usuario, "ativo", false);
        mvc.perform(get("/api/auth/me").header("Authorization", bearer())).andExpect(status().isUnauthorized());
    }
    @Test void tokenDeContaExcluidaNaoValeParaContaRecriada() throws Exception {
        String antigo = bearer();
        ReflectionTestUtils.setField(usuario, "id", 99L);
        mvc.perform(get("/api/auth/me").header("Authorization", antigo)).andExpect(status().isUnauthorized());
    }
    @Test void consultaPerfilSemExporHash() throws Exception {
        mvc.perform(get("/api/auth/me").header("Authorization", bearer()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.senhaHash").doesNotExist()).andExpect(jsonPath("$.tokenConfirmacao").doesNotExist());
    }
    @Test void cadastroValido() throws Exception {
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Maria\",\"email\":\"maria@example.com\",\"senha\":\"segredo\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.sucesso").value(true));
        verify(usuarioService).cadastrar(any());
    }
    @ParameterizedTest @ValueSource(strings = {"{}", "{\"nome\":\"Ma\",\"email\":\"invalido\",\"senha\":\"1\"}"})
    void cadastroInvalidoNaoChegaAoServico(String body) throws Exception {
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
        verifyNoInteractions(usuarioService);
    }
    @Test void loginValidoRetornaToken() throws Exception {
        when(authService.autenticar(any())).thenReturn(usuario);
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"maria@example.com\",\"senha\":\"segredo\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.usuario.senhaHash").doesNotExist());
    }
    @Test void loginInvalidoRetorna401() throws Exception {
        when(authService.autenticar(any())).thenThrow(new BadCredentialsException("E-mail ou senha inválidos."));
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"maria@example.com\",\"senha\":\"errada\"}")).andExpect(status().isUnauthorized());
    }
    @Test void confirmaEmail() throws Exception {
        when(confirmationService.confirmarEmail("valido")).thenReturn(true);
        mvc.perform(get("/api/auth/confirmar").param("token", "valido")).andExpect(status().isOk());
        mvc.perform(get("/api/auth/confirmar").param("token", "expirado")).andExpect(status().isBadRequest());
    }
    @Test void recuperacaoAceitaAcessoPublicoEValidaDados() throws Exception {
        mvc.perform(post("/api/auth/forgot-password").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"maria@example.com\"}")).andExpect(status().isOk());
        verify(recuperacaoSenhaService).solicitar("maria@example.com");
        mvc.perform(post("/api/auth/forgot-password").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"invalido\"}")).andExpect(status().isBadRequest());
    }
    @Test void redefinicaoRejeitaTokenInvalidoEConfirmaSucesso() throws Exception {
        when(recuperacaoSenhaService.redefinir("valido", "segredo")).thenReturn(true);
        mvc.perform(post("/api/auth/reset-password").contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"invalido\",\"senha\":\"segredo\"}")).andExpect(status().isBadRequest());
        mvc.perform(post("/api/auth/reset-password").contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"valido\",\"senha\":\"segredo\"}")).andExpect(status().isOk());
    }
    @Test void trocarSenhaInvalidaJwtAntigo() throws Exception {
        String antigo = bearer();
        usuario.redefinirSenha("novo-hash");
        mvc.perform(get("/api/auth/me").header("Authorization", antigo)).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/auth/me").header("Authorization", bearer())).andExpect(status().isOk());
    }
    @Test void recuperacaoLimitaTentativasPorEndereco() throws Exception {
        for (int i = 0; i < 10; i++) {
            mvc.perform(post("/api/auth/forgot-password").with(r -> { r.setRemoteAddr("192.0.2.55"); return r; })
                    .contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"maria@example.com\"}")).andExpect(status().isOk());
        }
        mvc.perform(post("/api/auth/forgot-password").with(r -> { r.setRemoteAddr("192.0.2.55"); return r; })
                .contentType(MediaType.APPLICATION_JSON).content("{\"email\":\"maria@example.com\"}")).andExpect(status().isTooManyRequests());
    }
    @Test void redefinicaoRejeitaSenhaAcimaDoLimiteDoBcrypt() throws Exception {
        mvc.perform(post("/api/auth/reset-password").contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\":\"valido\",\"senha\":\"" + "á".repeat(37) + "\"}"))
                .andExpect(status().isBadRequest());
    }
    @Test void listaSomenteDadosDoPrincipal() throws Exception {
        when(animalService.listarDoUsuario(usuario)).thenReturn(List.of());
        mvc.perform(get("/api/animais").header("Authorization", bearer())).andExpect(status().isOk()).andExpect(content().json("[]"));
        verify(animalService).listarDoUsuario(usuario);
    }
    @Test void criaAnimalSemSerializarDono() throws Exception {
        when(animalService.criar(eq(usuario), any())).thenReturn(new Animal(usuario, "A1", "Mimosa", Categoria.BEZERRA, 0.0));
        mvc.perform(post("/api/animais").header("Authorization", bearer()).contentType(MediaType.APPLICATION_JSON).content(animalJson()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Mimosa")).andExpect(jsonPath("$.usuario").doesNotExist());
    }
    @Test void atualizaAnimal() throws Exception {
        when(animalService.atualizar(eq(usuario), eq(1L), any())).thenReturn(new Animal(usuario, "A1", "Mimosa", Categoria.BEZERRA, 0.0));
        mvc.perform(put("/api/animais/1").header("Authorization", bearer()).contentType(MediaType.APPLICATION_JSON).content(animalJson()))
                .andExpect(status().isOk());
    }
    @Test void rejeitaAnimalInvalido() throws Exception {
        mvc.perform(post("/api/animais").header("Authorization", bearer()).contentType(MediaType.APPLICATION_JSON)
                .content(animalJson().replace("0.0", "-1.0"))).andExpect(status().isBadRequest());
        verifyNoInteractions(animalService);
    }
    @Test void excluiAnimal() throws Exception {
        mvc.perform(delete("/api/animais/1").header("Authorization", bearer())).andExpect(status().isNoContent());
        verify(animalService).excluir(usuario, 1L);
    }
    @Test void excluiContaComSenha() throws Exception {
        mvc.perform(delete("/api/usuarios/me").header("Authorization", bearer()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"senha\":\"segredo\"}")).andExpect(status().isOk());
        verify(usuarioService).excluirConta(usuario, "segredo");
    }
    @Test void exclusaoExigeSenha() throws Exception {
        mvc.perform(delete("/api/usuarios/me").header("Authorization", bearer()).contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andExpect(status().isBadRequest());
        verifyNoInteractions(usuarioService);
    }
    @Test void erroInternoNaoVazaDetalhes() throws Exception {
        when(animalService.listarDoUsuario(any())).thenThrow(new IllegalStateException("senha-do-banco SQL select"));
        mvc.perform(get("/api/animais").header("Authorization", bearer())).andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensagem").value("Erro interno. Tente novamente mais tarde."));
    }
    @Test void jsonMalformadoRetorna400() throws Exception {
        mvc.perform(post("/api/usuarios").contentType(MediaType.APPLICATION_JSON).content("{")).andExpect(status().isBadRequest());
    }
    @Test void metodoNaoSuportadoPreserva405() throws Exception {
        mvc.perform(patch("/api/animais").header("Authorization", bearer())).andExpect(status().isMethodNotAllowed());
    }
    @Test void tipoDeConteudoIncorretoPreserva415() throws Exception {
        mvc.perform(post("/api/usuarios").contentType(MediaType.TEXT_PLAIN).content("texto")).andExpect(status().isUnsupportedMediaType());
    }
    @Test void confirmacaoExigeToken() throws Exception {
        mvc.perform(get("/api/auth/confirmar")).andExpect(status().isBadRequest());
    }
    @Test void permiteFrontendConfigurado() throws Exception {
        mvc.perform(options("/api/animais").header("Origin", "https://frontend.example.com").header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk()).andExpect(header().string("Access-Control-Allow-Origin", "https://frontend.example.com"));
    }
    @ParameterizedTest @ValueSource(strings = {"https://atacante.vercel.app", "https://atacante.duckdns.org"})
    void bloqueiaOrigensDeTerceiros(String origin) throws Exception {
        mvc.perform(options("/api/animais").header("Origin", origin).header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isForbidden()).andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }
    static String animalJson() { return "{\"brinco\":\"A1\",\"nome\":\"Mimosa\",\"categoria\":\"BEZERRA\",\"producaoDiaria\":0.0}"; }
}
