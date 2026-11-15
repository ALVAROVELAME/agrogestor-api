package com.agrogestor.api.service;

import com.agrogestor.api.config.JwtProperties;
import com.agrogestor.api.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.*;

class JwtServiceTest {
    static final String SECRET = "chave-exclusiva-de-testes-com-mais-de-32-bytes";
    static JwtService service(String secret, String issuer, long expiration) {
        JwtProperties props = new JwtProperties(); props.setSecret(secret);
        props.setIssuer(issuer); props.setExpiration(expiration); return new JwtService(props);
    }
    @Test void geraTokenComIdentidadeEAssinatura() {
        JwtService service = service(SECRET, "teste", 60000);
        Usuario u = new Usuario("Maria", "maria@example.com", "hash");
        ReflectionTestUtils.setField(u, "id", 7L);
        String token = service.gerarToken(u);
        assertThat(service.isTokenValido(token)).isTrue();
        assertThat(service.extrairEmail(token)).isEqualTo(u.getEmail());
    }
    @Test void rejeitaTokenExpirado() {
        String token = Jwts.builder().subject("maria@example.com").issuer("teste")
                .expiration(new Date(0)).signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8))).compact();
        assertThat(service(SECRET, "teste", 60000).isTokenValido(token)).isFalse();
    }
    @Test void rejeitaAssinaturaDiferente() {
        String token = service(SECRET, "teste", 60000).gerarToken(new Usuario("Maria", "maria@example.com", "hash"));
        assertThat(service(SECRET + "outra", "teste", 60000).isTokenValido(token)).isFalse();
    }
    @Test void rejeitaIssuerDiferente() {
        String token = service(SECRET, "outro", 60000).gerarToken(new Usuario("Maria", "maria@example.com", "hash"));
        assertThat(service(SECRET, "teste", 60000).isTokenValido(token)).isFalse();
    }
    @Test void rejeitaTokenMalformadoOuVazio() {
        JwtService service = service(SECRET, "teste", 60000);
        assertThat(service.isTokenValido("invalido")).isFalse();
        assertThat(service.isTokenValido("")).isFalse();
        assertThat(service.isTokenValido(null)).isFalse();
    }
    @Test void rejeitaConfiguracaoInsegura() {
        assertThatThrownBy(() -> service("curta", "teste", 60000)).isInstanceOf(io.jsonwebtoken.security.WeakKeyException.class);
        assertThatThrownBy(() -> service(SECRET, "", 60000)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service(SECRET, "teste", 0)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void claimsExigemIdentidadeEExpiracao() {
        JwtService service = service(SECRET, "teste", 60000);
        String token = service.gerarToken(new Usuario("Maria", "maria@example.com", "hash"));
        assertThatThrownBy(() -> service.validarClaims(token)).isInstanceOf(IllegalArgumentException.class);
        String semExpiracao = Jwts.builder().subject("maria@example.com").claim("id", 7L).issuer("teste")
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8))).compact();
        assertThatThrownBy(() -> service.validarClaims(semExpiracao)).isInstanceOf(IllegalArgumentException.class);
    }
}
