package com.agrogestor.api.controller.conta;

import com.agrogestor.api.dto.MensagemRespostaDTO;
import com.agrogestor.api.dto.auth.RedefinirSenhaDTO;
import com.agrogestor.api.dto.auth.SolicitarRecuperacaoDTO;
import com.agrogestor.api.service.auth.RecuperacaoSenhaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RecuperacaoSenhaController {
    private final RecuperacaoSenhaService service;
    private final Map<String, Limite> limites = new HashMap<>();
    private record Limite(Instant inicio, int tentativas) {}

    public RecuperacaoSenhaController(RecuperacaoSenhaService service) { this.service = service; }

    private synchronized boolean permitir(String ip) {
        Instant agora = Instant.now();
        limites.entrySet().removeIf(e -> e.getValue().inicio().plusSeconds(900).isBefore(agora));
        Limite atual = limites.get(ip);
        if (atual == null) {
            if (limites.size() >= 10000) return false;
            limites.put(ip, new Limite(agora, 1));
            return true;
        }
        if (atual.tentativas() >= 10) return false;
        limites.put(ip, new Limite(atual.inicio(), atual.tentativas() + 1));
        return true;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MensagemRespostaDTO> solicitar(@Valid @RequestBody SolicitarRecuperacaoDTO dto, HttpServletRequest request) {
        if (!permitir(request.getRemoteAddr())) return limiteAtingido();
        try {
            service.solicitar(dto.email());
        } catch (MailException ex) {
            LoggerFactory.getLogger(getClass()).warn("Falha ao enviar e-mail de recuperação", ex);
        }
        return ResponseEntity.ok(new MensagemRespostaDTO(true, "Se houver uma conta com esse e-mail, enviaremos as instruções."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MensagemRespostaDTO> redefinir(@Valid @RequestBody RedefinirSenhaDTO dto, HttpServletRequest request) {
        if (!permitir(request.getRemoteAddr())) return limiteAtingido();
        if (!service.redefinir(dto.token(), dto.senha())) {
            return ResponseEntity.badRequest().body(new MensagemRespostaDTO(false, "Link inválido ou expirado."));
        }
        return ResponseEntity.ok(new MensagemRespostaDTO(true, "Senha redefinida. Faça login novamente."));
    }

    private ResponseEntity<MensagemRespostaDTO> limiteAtingido() {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new MensagemRespostaDTO(false, "Muitas tentativas. Tente novamente mais tarde."));
    }
}
