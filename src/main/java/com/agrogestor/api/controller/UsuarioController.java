package com.agrogestor.api.controller;

import com.agrogestor.api.dto.UsuarioCadastroDTO;
import com.agrogestor.api.dto.UsuarioRespostaDTO;
import com.agrogestor.api.model.Usuario;
import com.agrogestor.api.service.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioRespostaDTO> criar(
            @RequestBody UsuarioCadastroDTO dto
    ) {

        Usuario usuario = usuarioService.cadastrar(dto);

        return ResponseEntity.ok(
                new UsuarioRespostaDTO(usuario)
        );

    }

}