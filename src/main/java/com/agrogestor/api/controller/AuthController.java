package com.agrogestor.api.controller;

import com.agrogestor.api.dto.LoginDTO;
import com.agrogestor.api.dto.LoginRespostaDTO;
import com.agrogestor.api.dto.UsuarioRespostaDTO;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.service.AuthService;
import com.agrogestor.api.service.EmailConfirmationService;
import com.agrogestor.api.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final EmailConfirmationService emailConfirmationService;

    public AuthController(
            AuthService authService,
            JwtService jwtService,
            EmailConfirmationService emailConfirmationService
    ) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.emailConfirmationService = emailConfirmationService;
    }

    // ---------- LOGIN ----------
    @PostMapping("/login")
    public ResponseEntity<LoginRespostaDTO> login(
            @Valid @RequestBody LoginDTO dto
    ) {
        Usuario usuario = authService.autenticar(dto);
        String token = jwtService.gerarToken(usuario);

        return ResponseEntity.ok(
                new LoginRespostaDTO(
                        true,
                        "Login realizado com sucesso",
                        token,
                        new UsuarioRespostaDTO(usuario)
                )
        );
    }

    // ---------- ME (usuário logado) ----------
    @GetMapping("/me")
    public ResponseEntity<UsuarioRespostaDTO> me(
            @AuthenticationPrincipal Usuario usuario
    ) {
        if (usuario == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new UsuarioRespostaDTO(usuario));
    }

    // ---------- CONFIRMAR E-MAIL ----------
    @GetMapping("/confirmar")
    public ResponseEntity<LoginRespostaDTO> confirmarEmail(
            @RequestParam String token
    ) {
        boolean confirmado = emailConfirmationService.confirmarEmail(token);

        if (confirmado) {
            return ResponseEntity.ok(
                    new LoginRespostaDTO(true, "E-mail confirmado com sucesso")
            );
        }

        return ResponseEntity.badRequest()
                .body(new LoginRespostaDTO(false, "Token inválido ou expirado"));
    }
}