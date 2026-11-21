package com.agrogestor.api.controller.conta;

import com.agrogestor.api.dto.usuario.ExcluirContaDTO;
import com.agrogestor.api.dto.MensagemRespostaDTO;
import com.agrogestor.api.dto.usuario.UsuarioCadastroDTO;
import com.agrogestor.api.model.conta.Usuario;
import com.agrogestor.api.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<MensagemRespostaDTO> criar(
            @Valid @RequestBody UsuarioCadastroDTO dto
    ) {
        usuarioService.cadastrar(dto);

        return ResponseEntity.ok(
                new MensagemRespostaDTO(
                        true,
                        "Cadastro iniciado. Verifique seu email para confirmar a conta."
                )
        );
    }

    /**
     * Exclui a conta do usuário autenticado.
     * Requer a senha atual no body para confirmação.
     */
    @DeleteMapping("/me")
    public ResponseEntity<MensagemRespostaDTO> excluirConta(
            @AuthenticationPrincipal Usuario usuario,
            @Valid @RequestBody ExcluirContaDTO dto
    ) {
        usuarioService.excluirConta(usuario, dto.getSenha());

        return ResponseEntity.ok(
                new MensagemRespostaDTO(true, "Conta excluída com sucesso.")
        );
    }
}