package com.agrogestor.api.service;

import com.agrogestor.api.config.JwtProperties;
import com.agrogestor.api.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties props;
    private final SecretKey key;

    public JwtService(JwtProperties props) {
        this.props = props;
        this.key = Keys.hmacShaKeyFor(
                props.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }

    public String gerarToken(Usuario usuario) {
        Date agora = new Date();
        Date expiraEm = new Date(agora.getTime() + props.getExpiration());

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("nome", usuario.getNome())
                .issuer(props.getIssuer())
                .issuedAt(agora)
                .expiration(expiraEm)
                .signWith(key)
                .compact();
    }

    public String extrairEmail(String token) {
        return parsear(token).getSubject();
    }

    public boolean isTokenValido(String token) {
        try {
            Claims claims = parsear(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parsear(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(props.getIssuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}